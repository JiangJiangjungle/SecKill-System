package com.jsj.mapper;

import com.jsj.bean.Product;
import com.jsj.bean.Record;
import com.jsj.bean.User;
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

    boolean decreaseProductStock(Integer productId);

    boolean addUser(User user);
}
