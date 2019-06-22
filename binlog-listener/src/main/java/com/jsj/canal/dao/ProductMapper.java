package com.jsj.canal.dao;


import com.jsj.api.entity.ProductDO;
import com.jsj.api.exception.DAOException;
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
     * 获取所有商品库存
     *
     * @return
     */
    List<ProductDO> getAllStock(@Param("start") int start, @Param("end") int end) throws DAOException;
}
