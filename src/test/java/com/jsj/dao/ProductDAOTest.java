package com.jsj.dao;

import com.jsj.pojo.entity.ProductDO;
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
public class ProductDAOTest {
    @Resource
    private ProductMapper productMapper;

    @Test
    public void testAdd()throws Exception{
        UUID id = UUID.randomUUID();
        BigDecimal price = new BigDecimal("5999.9");
        String name = "huawei";
        ProductDO productDO = new ProductDO(id.toString(),1000,price,name, new Date());
        boolean finished = productMapper.addProduct(productDO);
    }

    @Test
    public void testDelete(){

    }

    @Test
    public void testUpdate()throws Exception{
        String id = "2bf55f51-6687-4587-8fd5-bb6c82fe8e7c";
        ProductDO productDO = productMapper.getProductById(id);
        if (productDO.getStock()>0){
            boolean finished = productMapper.updateStockByLock(id, productDO.getVersionId());
        }
    }

    @Test
    public void testSearchByPrimaryId()throws Exception{
        String id = "2bf55f51-6687-4587-8fd5-bb6c82fe8e7c";
        ProductDO productDO = productMapper.getProductById(id);
        System.out.println(productDO.toString());
    }

    @Test
    public void testSearch()throws Exception{
        List<ProductDO> productDOList = productMapper.getAllProducts(0,1000);
        productDOList.forEach(System.out::println);
    }
}
