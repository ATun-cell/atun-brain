package com.atun.brain.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 交易解析结果DTO（应用层）
 * 
 * @author lij
 * @date 2026/02/03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionParseResultDTO {
    
    /** 金额 */
    private BigDecimal amount;
    
    /** 类型 */
    private String type;  // INCOME | EXPENSE
    
    /** 分类ID */
    private Long categoryId;
    
    /** 分类名称 */
    private String categoryName;
    
    /** 描述 */
    private String description;
    
    /** 标签 */
    private List<String> tags;
    
    /** 交易日期 */
    private LocalDate transactionDate;
}
