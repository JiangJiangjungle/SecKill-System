package com.jsj.service.dao;


import com.jsj.api.entity.ProductDO;
import com.jsj.api.exception.DAOException;
import com.jsj.service.ServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
public class ProductMapperTest {
    private final Logger log = LoggerFactory.getLogger(ProductMapperTest.class);
    @Autowired
    ProductMapper productMapper;

    @Test
    public void testAddProduct() throws Exception {
        ProductDO productDO = new ProductDO(1000, new BigDecimal(66.50), "test", new Date());
        productMapper.addProduct(productDO);
    }

    /**
     * 更新商品库存
     *
     * @return
     */
    @Test
    public void testDecreaseStock() throws DAOException {
        productMapper.decreaseStock(1L);
    }

    /**
     * 利用乐观锁更新商品库存
     *
     * @return
     * @throws DAOException
     */
    @Test
    public void testDecreaseStockByVersionId() throws DAOException {
        productMapper.decreaseStockByVersionId(1L, 0);
    }

    /**
     * 根据主键查找
     *
     * @return
     */
    @Test
    public void testGetProductById() throws DAOException {
        ProductDO productDO = productMapper.getProductByPrimaryId(1L);
        System.out.println(productDO.toString());
    }

    /**
     * 根据主键获取版本id
     *
     * @return
     * @throws DAOException
     */
    @Test
    public void testGetVersionId() throws DAOException {
        int versionId = productMapper.getVersionIdByPrimaryId(1L);
        System.out.println(versionId);
    }

    @Test
    public void testGetStockById() throws DAOException {
        int stock = productMapper.getStockByPrimaryId(1L);
        System.out.println(stock);
    }

    /**
     * 获取所有商品
     *
     * @return
     */
    @Test
    public void testGetAllProducts() throws DAOException {
        List<ProductDO> products = productMapper.getAllProducts(0, 1000);
        for (ProductDO productDO : products) {
            System.out.println(productDO.toString());
        }
    }

    /**
     * 获取所有商品库存
     *
     * @return
     */
    @Test
    public void testGetAllStock() throws DAOException {
        List<ProductDO> products = productMapper.getAllStocks(0, 1000);
        for (ProductDO productDO : products) {
            System.out.println(productDO.toString());
        }
    }
}
