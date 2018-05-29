package com.jsj.atomicstock;

import com.jsj.entity.Product;
import com.jsj.mapper.PanicBuyingMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ProductAtomicStock {

    private Map<String, AtomicInteger> stockMap;

    @Resource
    private PanicBuyingMapper panicBuyingMapper;

    @PostConstruct
    private void init() {
        stockMap = new HashMap<>();
        List<Product> products = panicBuyingMapper.getAllProducts();
        int id;
        AtomicInteger atomicInteger;
        for (Product product : products) {
            id = product.getProductId();
            atomicInteger = new AtomicInteger(product.getStock());
            stockMap.put(id + "", atomicInteger);
        }
    }

    public AtomicInteger getStockById(Integer productId) {
        return stockMap.get(productId + "");
    }

}
