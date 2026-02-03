package com.atun.brain.infrastructure.persistence.mybatis.mapper;

import com.atun.brain.infrastructure.persistence.mybatis.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper {
    
    UserPO findById(@Param("id") Long id);
    
    UserPO findByUsername(@Param("username") String username);
    
    UserPO findByEmail(@Param("email") String email);
    
    List<UserPO> findAll();
    
    List<UserPO> findAllActive();
    
    List<UserPO> findByCreatedAfter(@Param("dateTime") LocalDateTime dateTime);
    
    List<UserPO> findByLastLoginAfter(@Param("dateTime") LocalDateTime dateTime);
    
    List<UserPO> findByEmailVerified(@Param("verified") boolean verified);
    
    List<UserPO> findByRole(@Param("role") String role);
    
    int insert(UserPO user);
    
    int update(UserPO user);
    
    int deleteById(@Param("id") Long id);
    
    int existsByUsername(@Param("username") String username);
    
    int existsByEmail(@Param("email") String email);
    
    long count();
    
    long countActive();
}