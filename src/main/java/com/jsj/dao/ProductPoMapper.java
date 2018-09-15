package com.jsj.dao;

import com.jsj.entity.ProductPO;
import com.jsj.exception.DAOException;

import java.util.List;

/**
 *
 * @author jiangshenjie
 * @date 2018-9-13
 */
public interface ProductPoMapper {

    /**
     * 添加商品
     * @param productPO
     * @return
     */
    boolean addProduct(ProductPO productPO)throws DAOException;

    /**
     * 商品库存减1
     * @param id
     * @return
     */
    boolean updateProductStock(String id)throws DAOException;

    /**
     * 根据主键查找
     * @param id
     * @return
     */
    ProductPO getProductById(String id)throws DAOException;

    /**
     * 获取所有商品
     * @return
     */
    List<ProductPO> getAllProducts()throws DAOException;
}
