package com.atun.brain.home.application.service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易响应 DTO
 *
 * @author atun-brain
 * @since 1.0
 */
@Data
@Builder
public class TransactionResponse {

    /**
     * 交易 ID
     */
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 交易类型
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
     * 分类名称
     */
    private String categoryName;

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
}
