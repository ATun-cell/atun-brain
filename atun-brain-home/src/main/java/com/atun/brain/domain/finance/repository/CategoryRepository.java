package com.atun.brain.domain.finance.repository;

import com.atun.brain.domain.finance.entity.Category;

import java.util.List;
import java.util.Optional;

/**
 * 分类仓储接口 - 领域层
 * 定义分类实体的持久化操作
 *
 * @author lij
 * @date 2026/02/03
 */
public interface CategoryRepository {
    
    /**
     * 保存分类
     */
    Category save(Category category);
    
    /**
     * 根据ID查找分类
     */
    Optional<Category> findById(Long id);
    
    /**
     * 根据名称和类型查找分类
     */
    Optional<Category> findByNameAndType(String name, String type);
    
    /**
     * 查找所有分类
     */
    List<Category> findAll();
    
    /**
     * 按类型查找分类
     */
    List<Category> findByType(String type);
    
    /**
     * 查找顶级分类（parentId为null）
     */
    List<Category> findRootCategories(String type);
    
    /**
     * 查找子分类
     */
    List<Category> findByParentId(Long parentId);
    
    /**
     * 查找分类树（包含所有子孙分类）
     */
    List<Category> findCategoryTree(String type);
    
    /**
     * 删除分类
     */
    void deleteById(Long id);
    
    /**
     * 更新分类
     */
    Category update(Category category);
}
