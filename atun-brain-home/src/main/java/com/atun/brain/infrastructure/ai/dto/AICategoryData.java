package com.atun.brain.infrastructure.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI分类数据（基础设施层DTO）
 * 
 * @author lij
 * @date 2026/02/03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AICategoryData {
    
    /** 身份证 */
    private Long id;
    
    /** 名称 */
    private String name;
    
    /** 类型 */
    private String type;  // EXPENSE | INCOME
    
    /** 父ID */
    private Long parentId;
}
