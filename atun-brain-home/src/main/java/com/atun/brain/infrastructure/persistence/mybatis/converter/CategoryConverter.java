package com.atun.brain.infrastructure.persistence.mybatis.converter;

import com.atun.brain.domain.finance.entity.Category;
import com.atun.brain.infrastructure.persistence.mybatis.po.CategoryPO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类实体与PO之间的转换器
 *
 * @author lij
 * @date 2026/02/03
 */
@Component
public class CategoryConverter {
    
    /**
     * 领域模型 -> PO（用于持久化）
     */
    public CategoryPO toPO(Category domain) {
        if (domain == null) {
            return null;
        }
        
        CategoryPO po = new CategoryPO();
        po.setId(domain.getId());
        po.setName(domain.getName());
        po.setType(domain.getType());
        po.setParentId(domain.getParentId());
        po.setCreatedAt(domain.getCreatedAt());
        po.setUpdatedAt(domain.getUpdatedAt());
        
        return po;
    }
    
    /**
     * PO -> 领域模型（从数据库加载）
     */
    public Category toDomain(CategoryPO po) {
        if (po == null) {
            return null;
        }
        
        Category domain = new Category();
        domain.setId(po.getId());
        domain.setName(po.getName());
        domain.setType(po.getType());
        domain.setParentId(po.getParentId());
        domain.setCreatedAt(po.getCreatedAt());
        domain.setUpdatedAt(po.getUpdatedAt());
        domain.setChildren(new ArrayList<>());
        
        return domain;
    }
    
    /**
     * PO列表 -> 领域模型列表
     */
    public List<Category> toDomainList(List<CategoryPO> pos) {
        if (pos == null) {
            return new ArrayList<>();
        }
        return pos.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * 构建分类树
     */
    public List<Category> buildTree(List<CategoryPO> pos) {
        List<Category> allCategories = toDomainList(pos);
        List<Category> rootCategories = new ArrayList<>();
        
        // 找出所有根分类
        for (Category category : allCategories) {
            if (category.isRoot()) {
                rootCategories.add(category);
            }
        }
        
        // 为每个根分类构建子树
        for (Category root : rootCategories) {
            buildSubTree(root, allCategories);
        }
        
        return rootCategories;
    }
    
    /**
     * 递归构建子树
     */
    private void buildSubTree(Category parent, List<Category> allCategories) {
        for (Category category : allCategories) {
            if (parent.getId().equals(category.getParentId())) {
                parent.addChild(category);
                buildSubTree(category, allCategories);
            }
        }
    }
}
