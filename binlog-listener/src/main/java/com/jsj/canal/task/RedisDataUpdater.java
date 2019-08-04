package com.jsj.canal.task;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.jsj.api.entity.ProductDO;
import com.jsj.api.entity.RecordDO;
import com.jsj.api.exception.DAOException;
import com.jsj.canal.config.CanalServerConfig;
import com.jsj.canal.config.CanalSubscribeConfig;
import com.jsj.canal.config.RedisConfig;
import com.jsj.canal.dao.ProductMapper;
import com.jsj.canal.dao.RecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangshenjie
 */
@Slf4j
@Component
public class RedisDataUpdater {
    /**
     * 分页查询限制
     */
    public final int LIMIT_MAX = 1000;

    @Autowired
    private CanalServerConfig canalServerConfig;
    @Autowired
    private RedisConfig redisConfig;
    @Autowired
    private CanalSubscribeConfig canalSubscribeConfig;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private JedisPool jedisPool;


    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor =
            new ScheduledThreadPoolExecutor(1);

    public void start() {
        try (Jedis jedis = jedisPool.getResource()) {
            try {
                preLoadData(jedis);
            } catch (DAOException d) {
                log.error("预加载数据出现异常：{}", d.getMessage());
            }
            scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
                // 创建链接
                CanalConnector connector = CanalConnectors.newSingleConnector(
                        new InetSocketAddress(canalServerConfig.getHost(), canalServerConfig.getPort()),
                        canalServerConfig.getDestination(), canalServerConfig.getUsername(), canalServerConfig.getPassword());
                try {

                    connector.connect();
                    connector.subscribe(canalServerConfig.getSubscribe());
                    connector.rollback();
                    // 获取指定数量的数据
                    Message message = connector.getWithoutAck(100);
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (size > 0) {
                        updateRedis(jedis, message.getEntries());
                    }
                    // 提交确认
                    connector.ack(batchId);
                } finally {
                    connector.disconnect();
                }

            }, 0L, 50L, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 缓存数据预加载
     *
     * @param jedis
     * @throws DAOException
     */
    private void preLoadData(Jedis jedis) throws DAOException {
        int count = 0;
        //刷新缓存
        jedis.flushDB();
        log.info("刷新缓存");
        List<RecordDO> recordDOList;
        List<ProductDO> productDOList;
        try {
            //加载交易记录
            do {
                recordDOList = recordMapper.getAllRecords(count, count + LIMIT_MAX);
                if (recordDOList == null || recordDOList.isEmpty()) {
                    break;
                }
                recordDOList.forEach((RecordDO recordDO) -> jedis.sadd(redisConfig.getProductUserListPrefix()
                        + recordDO.getProductId(), String.valueOf(recordDO.getUserId())));
                log.info("加载用户秒杀记录，第 {}-{} 项到redis缓存", count, count + recordDOList.size());
                count += LIMIT_MAX;
            } while (recordDOList.size() == LIMIT_MAX);
            //加载库存记录
            count = 0;
            do {
                productDOList = productMapper.getAllStock(count, count + LIMIT_MAX);
                if (productDOList == null || productDOList.isEmpty()) {
                    break;
                }
                for (ProductDO productDO : productDOList) {
                    jedis.set(redisConfig.getProductStockPrefix() + productDO.getId(), String.valueOf(productDO.getStock()));
                }
                log.info("加载库存记录，第 {}-{} 项到redis缓存", count, count + productDOList.size());
                count += LIMIT_MAX;
            } while (productDOList.size() == LIMIT_MAX);
        } catch (DAOException d) {
            throw new DAOException("加载交易和库存记录数据时失败");
        }
    }

    private void updateRedis(Jedis jedis, List<Entry> entries) {
        entries.stream()
                .filter(entry -> entry.getEntryType() == EntryType.ROWDATA)
                .forEach((entry) -> {
                    RowChange rowChange;
                    try {
                        rowChange = RowChange.parseFrom(entry.getStoreValue());
                    } catch (Exception e) {
                        throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
                    }
                    CanalEntry.EventType eventType = rowChange.getEventType();
                    Header header = entry.getHeader();
                    log.info("======>binlog[{}:{}] , name[{},{}] , eventType : {}", header.getLogfileName(),
                            header.getLogfileOffset(), header.getSchemaName(), header.getTableName(), eventType);
                    //检查update和insert记录
                    switch (eventType) {
                        case UPDATE:
                            for (RowData rowData : rowChange.getRowDatasList()) {
                                List<Column> columns = rowData.getAfterColumnsList();
                                String productId = "";
                                String stock = "";
                                boolean needUpdate = false;
                                for (Column column : columns) {
                                    if (column.getName().equals(canalSubscribeConfig.getProductId())) {
                                        productId = column.getValue();
                                    }
                                    if (column.getName().equals(canalSubscribeConfig.getProductStock()) && (needUpdate = column.getUpdated())) {
                                        stock = column.getValue();
                                    }
                                }
                                if (needUpdate) {
                                    // 更新缓存中的库存数量
                                    jedis.set(redisConfig.getProductStockPrefix() + productId, stock);
                                    log.info("商品id：{} ,redis的库存更新：{}", productId, stock);
                                }
                            }
                            break;
                        case INSERT:
                            for (RowData rowData : rowChange.getRowDatasList()) {
                                List<Column> columns = rowData.getAfterColumnsList();
                                String productId = "";
                                String userId = "";
                                for (Column column : columns) {
                                    if (column.getName().equals(canalSubscribeConfig.getRecordProductId())) {
                                        productId = column.getValue();
                                    }
                                    if (column.getName().equals(canalSubscribeConfig.getRecordUserId())) {
                                        userId = column.getValue();
                                    }
                                }
                                // 更新秒杀名单
                                jedis.sadd(redisConfig.getProductUserListPrefix() + productId, userId);
                                log.info("Redis秒杀名单更新. productID: {},userID: {}", productId, userId);
                            }
                            break;
                        default:
                            break;
                    }
                });
    }
}
