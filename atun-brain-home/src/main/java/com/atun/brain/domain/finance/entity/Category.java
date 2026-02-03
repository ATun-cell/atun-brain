package com.atun.brain.domain.finance.entity;

import com.atun.brain.domain.shared.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 类别实体 - 支持树形结构的分类系统
 * 例：餐饮 -> 中餐 -> 火锅
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseEntity {
    
    /** 分类名称 */
    private String name;
    
    /** 分类类型（EXPENSE/INCOME） */
    private String type;
    
    /** 父分类ID（顶级分类为null） */
    private Long parentId;
    
    /** 子分类列表（用于查询结果） */
    private List<Category> children;
    
    /**
     * 创建顶级分类
     */
    public static Category createRoot(String name, String type) {
        Category category = new Category();
        category.setName(name);
        category.setType(type);
        category.setParentId(null);
        category.setChildren(new ArrayList<>());
        
        category.validate();
        return category;
    }
    
    /**
     * 创建子分类
     */
    public static Category createChild(String name, String type, Long parentId) {
        Category category = new Category();
        category.setName(name);
        category.setType(type);
        category.setParentId(parentId);
        category.setChildren(new ArrayList<>());
        
        category.validate();
        category.validateParent();
        return category;
    }
    
    /**
     * 判断是否为顶级分类
     */
    public boolean isRoot() {
        return parentId == null;
    }
    
    /**
     * 判断是否为叶子节点（无子分类）
     */
    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }
    
    /**
     * 添加子分类
     */
    public void addChild(Category child) {
        if (child == null) {
            throw new IllegalArgumentException("子分类不能为空");
        }
        if (!this.type.equals(child.getType())) {
            throw new IllegalArgumentException("子分类类型必顾与父分类一致");
        }
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }
    
    /**
     * 获取所有后代分类ID（包含子孙分类）
     */
    public List<Long> getAllDescendantIds() {
        List<Long> ids = new ArrayList<>();
        if (this.id != null) {
            ids.add(this.id);
        }
        if (this.children != null) {
            for (Category child : this.children) {
                ids.addAll(child.getAllDescendantIds());
            }
        }
        return ids;
    }
    
    /**
     * 验证分类数据
     */
    private void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("分类名称不能为空");
        }
        if (type == null || (!"EXPENSE".equals(type) && !"INCOME".equals(type))) {
            throw new IllegalArgumentException("分类类型必须为EXPENSE或INCOME");
        }
    }
    
    /**
     * 验证父分类
     */
    private void validateParent() {
        if (!isRoot() && parentId == null) {
            throw new IllegalArgumentException("非顶级分类必须指定父分类ID");
        }
    }
}
