package com.jsj;

import com.jsj.atomicstock.ProductAtomicStock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AtomicStockTest {

    @Resource
    private ProductAtomicStock productAtomicStock;

    @Test
    public void test() {
        int id = 3;
        AtomicInteger atomicInteger = productAtomicStock.getStockById(id);
        System.out.println(atomicInteger.get());
    }
}
