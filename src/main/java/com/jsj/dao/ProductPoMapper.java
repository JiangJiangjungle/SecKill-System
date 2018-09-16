package com.jsj.dao;

import com.jsj.entity.ProductPO;
import com.jsj.exception.DAOException;
import org.apache.ibatis.annotations.Param;

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
     * 利用乐观锁更新商品库存
     * @param id
     * @return
     */
    boolean updateProductStock(@Param("id")String id,@Param("versionId") Integer versionId)throws DAOException;

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
