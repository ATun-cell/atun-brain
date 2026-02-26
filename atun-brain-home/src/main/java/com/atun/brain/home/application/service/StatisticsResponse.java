package com.atun.brain.home.application.service;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 收支统计响应 DTO
 *
 * @author atun-brain
 * @since 1.0
 */
@Data
@Builder
public class StatisticsResponse {

    /**
     * 总收入
     */
    private BigDecimal totalIncome;

    /**
     * 总支出
     */
    private BigDecimal totalExpense;

    /**
     * 结余
     */
    private BigDecimal balance;

    /**
     * 交易笔数
     */
    private Integer transactionCount;
}
