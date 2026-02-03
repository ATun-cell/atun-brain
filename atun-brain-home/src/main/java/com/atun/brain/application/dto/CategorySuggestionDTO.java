package com.atun.brain.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类推荐结果DTO（应用层）
 * 
 * @author lij
 * @date 2026/02/03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorySuggestionDTO {
    
    /** 分类ID */
    private Long categoryId;
    
    /** 分类名称 */
    private String categoryName;
    
    /** 推荐理由 */
    private String reason;
    
    /** 置信度（0-1） */
    private Double confidence;
}
