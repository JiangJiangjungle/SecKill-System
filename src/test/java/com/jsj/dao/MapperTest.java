package com.jsj.dao;

import com.jsj.entity.ProductPO;
import com.jsj.entity.RecordPO;
import com.jsj.entity.UserPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {

    @Resource
    private PanicBuyingMapper panicBuyingMapper;

    @Test
    public void test() {
        UserPO userPO = panicBuyingMapper.getUserById(1);
        System.out.println(userPO.toString());

        List<ProductPO> productPOS = panicBuyingMapper.getAllProducts();
        for (ProductPO productPO : productPOS) {
            System.out.println(productPO.getProductName());
        }
    }

    @Test
    public void test2() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        RecordPO recordPO = new RecordPO(null, userId.toString(), productId.toString(), 1, new Date());
        panicBuyingMapper.addRecord(recordPO);
    }

    @Test
    public void test4() {
        UserPO userPO = new UserPO();
        userPO.setPhone("13855558888");
        String name = "tom_";
        for (int i = 0; i < 1000; i++) {
            userPO.setUserName(name + i);
            System.out.println(i + ": " + panicBuyingMapper.addUser(userPO));
        }
    }


    @Test
    public void test3() {
        List<RecordPO> recordPOS = panicBuyingMapper.getAllRecords();
        for (RecordPO recordPO : recordPOS) {
            System.out.println(recordPO.toString());
        }
    }

}
