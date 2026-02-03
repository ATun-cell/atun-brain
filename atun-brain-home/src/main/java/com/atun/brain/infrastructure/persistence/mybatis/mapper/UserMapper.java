package com.atun.brain.infrastructure.persistence.mybatis.mapper;

import com.atun.brain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    
    User findById(@Param("id") Long id);
    
    User findByUsername(@Param("username") String username);
    
    User findByEmail(@Param("email") String email);
    
    int insert(User user);
    
    int update(User user);
    
    int deleteById(@Param("id") Long id);
}