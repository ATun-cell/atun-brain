package com.atun.brain.infrastructure.persistence.mybatis.po;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 交易采购订单
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
public class TransactionPO {
    
    private Long id;
    private Long userId;
    private Long messageId;
    private BigDecimal amount;
    private String type;
    private Long categoryId;
    private LocalDate transactionDate;
    private String description;
    private String tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
