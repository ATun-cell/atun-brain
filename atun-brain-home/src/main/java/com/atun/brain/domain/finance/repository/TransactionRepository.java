package com.atun.brain.domain.finance.repository;

import com.atun.brain.domain.finance.aggregate.Transaction;
import com.atun.brain.domain.finance.valueobject.Period;

import java.util.List;
import java.util.Optional;

/**
 * 交易仓储接口 - 领域层
 * 定义交易聚合根的持久化操作
 *
 * @author lij
 * @date 2026/02/03
 */
public interface TransactionRepository {
    
    /**
     * 保存交易
     */
    Transaction save(Transaction transaction);
    
    /**
     * 根据ID查找交易
     */
    Optional<Transaction> findById(Long id);
    
    /**
     * 查找用户的所有交易
     */
    List<Transaction> findByUserId(Long userId);
    
    /**
     * 按时间范围查找交易
     */
    List<Transaction> findByUserIdAndPeriod(Long userId, Period period);
    
    /**
     * 按分类查找交易
     */
    List<Transaction> findByUserIdAndCategoryId(Long userId, Long categoryId);
    
    /**
     * 查找交易并包含分类信息
     */
    List<Transaction> findByUserIdWithCategory(Long userId, Period period);
    
    /**
     * 删除交易
     */
    void deleteById(Long id);
    
    /**
     * 更新交易
     */
    Transaction update(Transaction transaction);
}
