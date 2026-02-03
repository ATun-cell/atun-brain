package com.atun.brain.domain.finance.repository;

import com.atun.brain.domain.finance.aggregate.Budget;

import java.util.List;
import java.util.Optional;

/**
 * 预算仓储接口 - 领域层
 * 定义预算聚合根的持久化操作
 *
 * @author lij
 * @date 2026/02/03
 */
public interface BudgetRepository {
    
    /**
     * 保存预算
     */
    Budget save(Budget budget);
    
    /**
     * 根据ID查找预算
     */
    Optional<Budget> findById(Long id);
    
    /**
     * 查找用户的所有预算
     */
    List<Budget> findByUserId(Long userId);
    
    /**
     * 查找用户的月度预算
     */
    List<Budget> findMonthlyBudgets(Long userId, int year, int month);
    
    /**
     * 查找用户的周度预算
     */
    List<Budget> findWeeklyBudgets(Long userId);
    
    /**
     * 查找特定分类的预算
     */
    Optional<Budget> findByUserIdAndCategoryIdAndPeriod(
        Long userId, Long categoryId, String periodType, Integer year, Integer month);
    
    /**
     * 删除预算
     */
    void deleteById(Long id);
    
    /**
     * 更新预算
     */
    Budget update(Budget budget);
}
