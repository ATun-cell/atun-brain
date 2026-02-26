package com.atun.brain.home.domain.repository;

import com.atun.brain.home.domain.model.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 交易仓储接口
 * 定义在领域层，由基础设施层实现
 *
 * @author atun-brain
 * @since 1.0
 */
public interface TransactionRepository {

    /**
     * 保存交易
     *
     * @param transaction 交易实体
     * @return 保存后的实体
     */
    Transaction save(Transaction transaction);

    /**
     * 根据 ID 查找交易
     *
     * @param id 交易 ID
     * @return 交易实体
     */
    Optional<Transaction> findById(Long id);

    /**
     * 根据用户 ID 查找交易列表
     *
     * @param userId 用户 ID
     * @return 交易列表
     */
    List<Transaction> findByUserId(Long userId);

    /**
     * 根据时间范围查询交易
     *
     * @param userId 用户 ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 交易列表
     */
    List<Transaction> findByUserIdAndTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据分类 ID 查询交易
     *
     * @param userId 用户 ID
     * @param categoryId 分类 ID
     * @return 交易列表
     */
    List<Transaction> findByUserIdAndCategoryId(Long userId, Long categoryId);

    /**
     * 删除交易（逻辑删除）
     *
     * @param id 交易 ID
     */
    void delete(Long id);
}
