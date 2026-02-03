package com.atun.brain.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预算
 *
 * @author lij
 * @date 2026/02/02
 */
@Data
public class Budget {
    
    /** 身份证 */
    private Long id;
    
    /** 用户ID */
    private Long userId;
    
    /** 类别ID */
    private Long categoryId;
    
    /** 金额 */
    private BigDecimal amount;
    
    /** 就是这样 */
    private String period; // month/week
    
    /** 年份 */
    private Integer year;
    
    /** 月份 */
    private Integer month;
    
    /** 创建于 */
    private LocalDateTime createdAt;
}
