package com.atun.brain.infrastructure.ai.dto;

import lombok.Data;

/**
 * AI分类建议结果
 * 
 * @author lij
 * @date 2026/02/03
 */
@Data
public class AICategorySuggestion {
    
    /** 类别ID */
    private Long categoryId;
    
    /** 原因 */
    private String reason;
}
