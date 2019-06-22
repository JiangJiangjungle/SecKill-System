//package com.jsj.service.config;
//
//import com.jsj.api.mappers.ProductMapper;
//import com.jsj.api.mappers.RecordMapper;
//import com.jsj.api.mappers.UserMapper;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MybatisConfig {
//    @Bean
//    public ProductMapper productMapper(@Autowired SqlSessionFactory sqlSessionFactory) {
//        return sqlSessionFactory.openSession().getMapper(ProductMapper.class);
//    }
//
//    @Bean
//    public UserMapper userMapper(@Autowired SqlSessionFactory sqlSessionFactory) {
//        return sqlSessionFactory.openSession().getMapper(UserMapper.class);
//    }
//
//    @Bean
//    public RecordMapper recordMapper(@Autowired SqlSessionFactory sqlSessionFactory) {
//        return sqlSessionFactory.openSession().getMapper(RecordMapper.class);
//    }
//}
