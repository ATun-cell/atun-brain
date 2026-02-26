package com.atun.brain.home.domain.model.entity;

import com.atun.brain.home.domain.model.vo.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易实体 - 聚合根
 * 记录用户的每一笔收支交易
 *
 * @author atun-brain
 * @since 1.0
 */
@Data
@Builder
public class Transaction {

    /**
     * 交易 ID
     */
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 交易类型：INCOME(收入) / EXPENSE(支出)
     */
    private TransactionType type;

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
     * 是否已删除
     */
    private Boolean deleted;
}
