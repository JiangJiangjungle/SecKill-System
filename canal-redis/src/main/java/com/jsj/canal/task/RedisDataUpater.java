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
import com.jsj.canal.config.RedisConfig;
import com.jsj.canal.dao.ProductMapper;
import com.jsj.canal.dao.RecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RedisDataUpater {
    /**
     * 分页查询限制
     */
    public final int LIMIT_MAX = 1000;

    @Autowired
    private CanalServerConfig canalServerConfig;
    @Autowired
    private RedisConfig redisConfig;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private JedisPool jedisPool;

    public void start() {
        try (Jedis jedis = jedisPool.getResource()) {
            try {
                preLoadData(jedis);
            } catch (DAOException d) {
                log.error("预加载数据出现异常：{}", d.getMessage());
            }
            // 创建链接
            CanalConnector connector = CanalConnectors.newSingleConnector(
                    new InetSocketAddress(canalServerConfig.getHost(), canalServerConfig.getPort()),
                    canalServerConfig.getDestination(), canalServerConfig.getUsername(), canalServerConfig.getPassword());
            int batchSize = 1000;
            try {
                connector.connect();
                connector.subscribe(canalServerConfig.getSubscribe());
                connector.rollback();
                while (true) {
                    // 获取指定数量的数据
                    Message message = connector.getWithoutAck(batchSize);
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (size > 0) {
                        updateRedis(jedis, message.getEntries());
                    }
                    // 提交确认
                    connector.ack(batchId);
                }
            } finally {
                connector.disconnect();
            }
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
                log.info("加载用户秒杀记录，第" + count + "-" + (count + recordDOList.size()) + "项到redis缓存");
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
                log.info("加载库存记录第" + count + "-" + (count + productDOList.size()) + "项到redis缓存");
                count += LIMIT_MAX;
            } while (productDOList.size() == LIMIT_MAX);
        } catch (DAOException d) {
            throw new DAOException("加载交易和库存记录数据时失败");
        }
    }

    private void updateRedis(Jedis jedis, List<Entry> entries) {
        for (CanalEntry.Entry entry : entries) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }
            RowChange rowChage = null;
            try {
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }
            CanalEntry.EventType eventType = rowChage.getEventType();
            Header header = entry.getHeader();
            log.info("======>binlog[{}:{}] , name[{},{}] , eventType : {}", header.getLogfileName(),
                    header.getLogfileOffset(), header.getSchemaName(), header.getTableName(), eventType);
            //检查update记录
            if (eventType == EventType.UPDATE) {
                for (RowData rowData : rowChage.getRowDatasList()) {
                    List<Column> columns = rowData.getAfterColumnsList();
                    String productId = null;
                    String stock = null;
                    boolean needUpdate = false;
                    for (Column column : columns) {
                        if (column.getName().equals(redisConfig.getId())) {
                            productId = column.getValue();
                        }
                        if (column.getName().equals(redisConfig.getStock()) && (needUpdate = column.getUpdated())) {
                            stock = column.getValue();
                        }
                    }
                    if (needUpdate) {
                        // 更新缓存中的库存数量
                        jedis.set(redisConfig.getProductStockPrefix() + productId, stock);
                        log.info("商品id：{} ,redis的库存更新：{}", productId, stock);
                    }
                }
            }
        }
    }
}
