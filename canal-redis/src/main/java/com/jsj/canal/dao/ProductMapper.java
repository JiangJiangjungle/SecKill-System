package com.jsj.canal.dao;

import com.jsj.app.exception.DAOException;
import com.jsj.app.pojo.entity.ProductDO;
import org.apache.ibatis.annotations.Param;
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
    boolean updateStock(@Param("id") String id) throws DAOException;

    /**
     * 利用乐观锁更新商品库存
     *
     * @param id        商品id
     * @param versionId 版本id
     * @return
     * @throws DAOException
     */
    boolean updateStockByLock(@Param("id") String id, @Param("versionId") Integer versionId) throws DAOException;

    /**
     * 根据主键查找
     *
     * @param id 商品id
     * @return
     */
    ProductDO getProductById(String id) throws DAOException;

    /**
     * 根据主键获取版本id
     *
     * @param id 商品id
     * @return
     * @throws DAOException
     */
    Integer getVersionId(String id) throws DAOException;

    Integer getStockById(String id) throws DAOException;

    /**
     * 获取所有商品
     *
     * @return
     */
    List<ProductDO> getAllProducts(@Param("start") int start, @Param("end") int end) throws DAOException;

    /**
     * 获取所有商品库存
     *
     * @return
     */
    List<ProductDO> getAllStock(@Param("start") int start, @Param("end") int end) throws DAOException;
}
