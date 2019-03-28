package com.jsj.canal.task;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.jsj.canal.config.CanalServerConfig;
import com.jsj.canal.config.RedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
@Component
public class RedisDataUpater {
    @Autowired
    private CanalServerConfig canalServerConfig;

    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private JedisPool jedisPool;

    public void start() {
        try (Jedis jedis = jedisPool.getResource()) {
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
                        jedis.hset(redisConfig.getStockRedisKey(), productId, stock);
                        log.info("商品id：{} ,redis的库存更新：{}", productId, stock);
                    }
                }
            }
        }
    }
}
