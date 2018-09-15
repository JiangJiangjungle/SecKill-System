package com.jsj.atomicstock;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ProductAtomicStock {

    private Map<String, AtomicInteger> stockMap;

    @Resource
    private PanicBuyingMapper panicBuyingMapper;

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
