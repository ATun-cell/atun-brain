package com.atun.brain.infrastructure.persistence.mybatis.mapper;

import com.atun.brain.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    
    Category findById(@Param("id") Long id);
    
    Category findByName(@Param("name") String name);
    
    List<Category> findAll();
    
    List<Category> findByType(@Param("type") String type);
    
    List<Category> findByParentId(@Param("parentId") Long parentId);
    
    int insert(Category category);
    
    int update(Category category);
}
