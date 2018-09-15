package com.jsj.util;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ProductAtomicStock {

    private Map<String, AtomicInteger> stockMap;

    @PostConstruct
    private void init() {
//        stockMap = new HashMap<>();
//        List<ProductPO> productPOS = panicBuyingMapper.getAllProducts();
//        int id;
//        AtomicInteger atomicInteger;
//        for (ProductPO productPO : productPOS) {
//            id = productPO.getProductId();
//            atomicInteger = new AtomicInteger(productPO.getStock());
//            stockMap.put(id + "", atomicInteger);
//        }
    }

    public AtomicInteger getStockById(Integer productId) {
        return stockMap.get(productId + "");
    }

}
