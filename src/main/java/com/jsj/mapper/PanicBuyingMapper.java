package com.jsj.mapper;

import com.jsj.entity.Product;
import com.jsj.entity.Record;
import com.jsj.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PanicBuyingMapper {

	Product getProductById(Integer id);

	User getUserById(Integer id);

	List<User> getAllUsers();

	List<Product> getAllProducts();

	List<Record> getAllRecords();

	boolean addRecord(Record record);

	boolean addUser(User user);

	boolean addProduct(Product product);

	boolean decreaseProductStock(Integer productId);
}
