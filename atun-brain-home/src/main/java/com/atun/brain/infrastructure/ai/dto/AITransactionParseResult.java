package com.atun.brain.infrastructure.ai.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * AI交易解析结果
 * 
 * @author lij
 * @date 2026/02/03
 */
@Data
public class AITransactionParseResult {
    
    /** 金额 */
    private Double amount;
    
    /** 类型 */
    private String type;  // INCOME | EXPENSE
    
    /** 类别ID */
    private Long categoryId;
    
    /** 描述 */
    private String description;
    
    /** 标签 */
    private List<String> tags;
    
    /** 交易日期 */
    private LocalDate transactionDate;
}
