package com.atun.brain.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 交易
 *
 * @author lij
 * @date 2026/02/02
 */
@Data
public class Transaction {
    
    /** 身份证 */
    private Long id;
    
    /** 用户ID */
    private Long userId;
    
    /** 消息ID */
    private Long messageId;
    
    /** 金额 */
    private BigDecimal amount;
    
    /** 类型 */
    private String type; // 支出/收入
    
    /** 类别ID */
    private Long categoryId;
    
    /** 交易日期 */
    private LocalDate transactionDate;
    
    /** 描述 */
    private String description;
    
    /** 标签 */
    private String tags; // JSON字符串
    
    /** 创建于 */
    private LocalDateTime createdAt;
    
    // 关联对象（用于查询结果）
    private Category category;
}
