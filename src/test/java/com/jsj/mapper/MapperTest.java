package com.jsj.mapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jsj.entity.Product;
import com.jsj.entity.Record;
import com.jsj.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {

	@Resource
	private PanicBuyingMapper panicBuyingMapper;

	@Test
	public void test() {
		User user = panicBuyingMapper.getUserById(1);
		System.out.println(user.toString());

		List<Product> products = panicBuyingMapper.getAllProducts();
		for (Product product : products) {
			System.out.println(product.getProductName());
		}
	}

	@Test
	public void test2() {
		Record record = new Record(666, 1, 1, new Date());
		String message = JSON.toJSONString(record);
		Record record2 = JSON.parseObject(message, new TypeReference<Record>() {
		});
		panicBuyingMapper.addRecord(record);
		System.out.println(record2.toString());
	}

	@Test
	public void test4() {
		User user = new User();
		user.setPhoneNumber("13855558888");
		String name = "tom_";
		for (int i = 0; i < 1000; i++) {
			user.setUserName(name + i);
			System.out.println(i+": "+panicBuyingMapper.addUser(user));
		}
	}


	@Test
	public void test3() {
		List<Record> records = panicBuyingMapper.getAllRecords();
		for (Record record : records) {
			System.out.println(record.toString());
		}
	}

}
