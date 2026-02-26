package com.atun.brain.home.application.service;

import com.atun.brain.home.application.service.dto.TransactionResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易应用服务
 * 负责应用层业务编排和事务管理
 *
 * @author atun-brain
 * @since 1.0
 */
public interface TransactionAppService {

    /**
     * 创建交易
     *
     * @param userId 用户 ID
     * @param type 交易类型：INCOME(收入) / EXPENSE(支出)
     * @param amount 金额
     * @param categoryId 分类 ID（可选）
     * @param description 交易描述
     * @param transactionTime 交易时间（可选，默认当前时间）
     * @return 交易响应
     */
    TransactionResponse createTransaction(Long userId, String type, BigDecimal amount,
                                          Long categoryId, String description, LocalDateTime transactionTime);

    /**
     * 根据 ID 查询交易
     *
     * @param id 交易 ID
     * @return 交易响应
     */
    TransactionResponse getTransactionById(Long id);

    /**
     * 查询用户交易列表
     *
     * @param userId 用户 ID
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @param type 交易类型（可选）
     * @param categoryId 分类 ID（可选）
     * @return 交易列表
     */
    List<TransactionResponse> listTransactions(Long userId, LocalDateTime startTime,
                                               LocalDateTime endTime, String type, Long categoryId);

    /**
     * 删除交易
     *
     * @param id 交易 ID
     */
    void deleteTransaction(Long id);

    /**
     * 获取收支统计
     *
     * @param userId 用户 ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    StatisticsResponse getStatistics(Long userId, String startTime, String endTime);
}
