package com.atun.brain.infrastructure.persistence.mybatis.mapper;

import com.atun.brain.infrastructure.persistence.mybatis.po.CategoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    
    CategoryPO findById(@Param("id") Long id);
    
    CategoryPO findByName(@Param("name") String name);
    
    List<CategoryPO> findAll();
    
    List<CategoryPO> findByType(@Param("type") String type);
    
    List<CategoryPO> findByParentId(@Param("parentId") Long parentId);
    
    int insert(CategoryPO category);
    
    int update(CategoryPO category);
}
