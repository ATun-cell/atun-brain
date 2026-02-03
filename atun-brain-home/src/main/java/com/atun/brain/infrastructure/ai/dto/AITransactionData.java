package com.atun.brain.infrastructure.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * AI交易数据（基础设施层DTO）
 * 
 * @author lij
 * @date 2026/02/03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AITransactionData {
    
    /** 身份证 */
    private Long id;
    
    /** 金额 */
    private Double amount;
    
    /** 类型 */
    private String type;  // EXPENSE | INCOME
    
    /** 类别名称 */
    private String categoryName;
    
    /** 交易日期 */
    private LocalDate transactionDate;
    
    /** 描述 */
    private String description;
}
