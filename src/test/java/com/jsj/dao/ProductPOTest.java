package com.jsj.dao;

import com.jsj.entity.ProductPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductPOTest {
    @Resource
    private ProductPoMapper productPoMapper;

    @Test
    public void testAdd()throws Exception{
        UUID id = UUID.randomUUID();
        BigDecimal price = new BigDecimal("5999.9");
        String name = "huawei";
        ProductPO productPO = new ProductPO(id.toString(),1000,price,name, new Date());
        boolean finished = productPoMapper.addProduct(productPO);
    }

    @Test
    public void testDelete(){

    }

    @Test
    public void testUpdate()throws Exception{
        String id = "2bf55f51-6687-4587-8fd5-bb6c82fe8e7c";
        ProductPO productPO = productPoMapper.getProductById(id);
        if (productPO.getStock()>0){
            boolean finished = productPoMapper.updateProductStock(id,productPO.getVersionId());
        }
    }

    @Test
    public void testSearchByPrimaryId()throws Exception{
        String id = "2bf55f51-6687-4587-8fd5-bb6c82fe8e7c";
        ProductPO productPO = productPoMapper.getProductById(id);
        System.out.println(productPO.toString());
    }

    @Test
    public void testSearch()throws Exception{
        List<ProductPO> productPOList = productPoMapper.getAllProducts();
        productPOList.forEach(System.out::println);
    }
}
