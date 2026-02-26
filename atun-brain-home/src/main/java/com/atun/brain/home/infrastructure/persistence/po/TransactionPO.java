package com.atun.brain.home.infrastructure.persistence.po;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易持久化对象
 * 对应数据库表：t_finance_transaction
 *
 * @author atun-brain
 * @since 1.0
 */
@Data
@Builder
public class TransactionPO {

    /**
     * 交易 ID
     */
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 交易类型：INCOME/EXPENSE
     */
    private String type;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 分类 ID
     */
    private Long categoryId;

    /**
     * 交易描述
     */
    private String description;

    /**
     * 交易时间
     */
    private LocalDateTime transactionTime;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 是否已删除：0-否，1-是
     */
    private Integer deleted;
}
