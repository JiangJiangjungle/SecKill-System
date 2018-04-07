package com.jsj;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jsj.bean.Product;
import com.jsj.bean.Record;
import com.jsj.bean.User;
import com.jsj.constant.ServiceRessult;
import com.jsj.mapper.PanicBuyingMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {

    @Resource
    private PanicBuyingMapper panicBuyingMapper;

    @Test
    public void test() {
        User user = panicBuyingMapper.getUserById(1);
        System.out.println(user.toString());

        List<Product> products = panicBuyingMapper.getAllProducts();
        for (Product product : products) {
            System.out.println(product.getProductName());
        }
    }

    @Test
    public void test2() {
        Record record = new Record(666, 999, ServiceRessult.SUCCESS.getValue(),
                ServiceRessult.SUCCESS.getLabel(), new Date());
        String message = JSON.toJSONString(record);
        Record record2 = JSON.parseObject(message, new TypeReference<Record>() {
        });
        panicBuyingMapper.addRecord(record);
        System.out.println(record2.toString());
    }

    @Test
    public void test3() {
        List<Record> records = panicBuyingMapper.getAllRecords();
        for (Record record:records) {
            System.out.println(record.toString());
        }
    }

}
