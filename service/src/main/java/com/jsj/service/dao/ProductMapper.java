package com.jsj.service.dao;

import com.jsj.api.entity.ProductDO;
import com.jsj.api.exception.DAOException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jiangshenjie
 * @date 2018-9-13
 */
@Repository
public interface ProductMapper {

    /**
     * 添加商品
     *
     * @param productDO
     * @return
     */
    boolean addProduct(ProductDO productDO) throws DAOException;

    /**
     * 更新商品库存
     *
     * @param id 商品id
     * @return
     */
    boolean decreaseStock(Long id) throws DAOException;

    /**
     * 利用乐观锁更新商品库存
     *
     * @param id        商品id
     * @param versionId 版本id
     * @return
     * @throws DAOException
     */
    boolean decreaseStockByVersionId(Long id, Integer versionId) throws DAOException;

    /**
     * 根据主键查找
     *
     * @param id 商品id
     * @return
     */
    ProductDO getProductByPrimaryId(Long id) throws DAOException;

    /**
     * 根据主键获取版本id
     *
     * @param id 商品id
     * @return
     * @throws DAOException
     */
    Integer getVersionIdByPrimaryId(Long id) throws DAOException;

    Integer getStockByPrimaryId(Long id) throws DAOException;

    /**
     * 获取所有商品
     *
     * @return
     */
    List<ProductDO> getAllProducts(Integer start, Integer end) throws DAOException;

    /**
     * 获取所有商品库存
     *
     * @return
     */
    List<ProductDO> getAllStocks(Integer start, Integer end) throws DAOException;
}
