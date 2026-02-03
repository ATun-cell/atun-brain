package com.atun.brain.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分类 DTO（应用层）
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    
    /** ID */
    private Long id;
    
    /** 名称 */
    private String name;
    
    /** 类型 */
    private String type;  // EXPENSE | INCOME
    
    /** 父级ID */
    private Long parentId;
    
    /** 子分类列表 */
    private List<CategoryDTO> children;
    
    /** 是否为根节点 */
    private Boolean isRoot;
    
    /** 是否为叶子节点 */
    private Boolean isLeaf;
}
