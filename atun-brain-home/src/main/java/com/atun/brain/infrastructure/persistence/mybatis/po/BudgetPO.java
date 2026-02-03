package com.atun.brain.infrastructure.persistence.mybatis.po;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预算邮政
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
public class BudgetPO {
    
    private Long id;
    private Long userId;
    private Long categoryId;
    private BigDecimal amount;
    private String periodType;
    private Integer year;
    private Integer month;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
