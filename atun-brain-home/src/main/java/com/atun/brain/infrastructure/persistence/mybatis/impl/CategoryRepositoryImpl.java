package com.atun.brain.infrastructure.persistence.mybatis.impl;

import com.atun.brain.domain.finance.entity.Category;
import com.atun.brain.domain.finance.repository.CategoryRepository;
import com.atun.brain.infrastructure.persistence.mybatis.converter.CategoryConverter;
import com.atun.brain.infrastructure.persistence.mybatis.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 分类仓储实现类 - 基础设施层
 * 使用MyBatis进行数据持久化
 *
 * @author lij
 * @date 2026/02/03
 */
@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    
    private final CategoryMapper categoryMapper;
    private final CategoryConverter categoryConverter;
    
    @Override
    public Category save(Category category) {
        com.atun.brain.infrastructure.persistence.mybatis.po.CategoryPO po = categoryConverter.toPO(category);
        
        if (category.isNew()) {
            categoryMapper.insert(po);
            category.setId(po.getId());
        } else {
            categoryMapper.update(po);
        }
        
        return category;
    }
    
    @Override
    public Optional<Category> findById(Long id) {
        com.atun.brain.infrastructure.persistence.mybatis.po.CategoryPO po = categoryMapper.findById(id);
        return Optional.ofNullable(categoryConverter.toDomain(po));
    }
    
    @Override
    public Optional<Category> findByNameAndType(String name, String type) {
        com.atun.brain.infrastructure.persistence.mybatis.po.CategoryPO po = categoryMapper.findByName(name);
        if (po != null && po.getType().equals(type)) {
            return Optional.of(categoryConverter.toDomain(po));
        }
        return Optional.empty();
    }
    
    @Override
    public List<Category> findAll() {
        List<com.atun.brain.infrastructure.persistence.mybatis.po.CategoryPO> pos = categoryMapper.findAll();
        return categoryConverter.toDomainList(pos);
    }
    
    @Override
    public List<Category> findByType(String type) {
        List<com.atun.brain.infrastructure.persistence.mybatis.po.CategoryPO> pos = categoryMapper.findByType(type);
        return categoryConverter.toDomainList(pos);
    }
    
    @Override
    public List<Category> findRootCategories(String type) {
        List<com.atun.brain.infrastructure.persistence.mybatis.po.CategoryPO> pos = categoryMapper.findByType(type);
        return categoryConverter.toDomainList(pos).stream()
            .filter(Category::isRoot)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Category> findByParentId(Long parentId) {
        List<com.atun.brain.infrastructure.persistence.mybatis.po.CategoryPO> pos = categoryMapper.findByParentId(parentId);
        return categoryConverter.toDomainList(pos);
    }
    
    @Override
    public List<Category> findCategoryTree(String type) {
        List<com.atun.brain.infrastructure.persistence.mybatis.po.CategoryPO> pos = categoryMapper.findByType(type);
        return categoryConverter.buildTree(pos);
    }
    
    @Override
    public void deleteById(Long id) {
        // 注意：实际业务中可能需要检查是否有子分类或关联的交易
        // 这里简化处理
        categoryMapper.update(categoryConverter.toPO(findById(id).orElse(null)));
    }
    
    @Override
    public Category update(Category category) {
        return save(category);
    }
}
