package com.atun.brain.infrastructure.persistence.mybatis.impl;

import com.atun.brain.domain.finance.aggregate.Budget;
import com.atun.brain.domain.finance.repository.BudgetRepository;
import com.atun.brain.infrastructure.persistence.mybatis.converter.BudgetConverter;
import com.atun.brain.infrastructure.persistence.mybatis.mapper.BudgetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 预算仓储实现类 - 基础设施层
 * 使用MyBatis进行数据持久化
 *
 * @author lij
 * @date 2026/02/03
 */
@Repository
@RequiredArgsConstructor
public class BudgetRepositoryImpl implements BudgetRepository {
    
    private final BudgetMapper budgetMapper;
    private final BudgetConverter budgetConverter;
    
    @Override
    public Budget save(Budget budget) {
        com.atun.brain.infrastructure.persistence.mybatis.po.BudgetPO po = budgetConverter.toPO(budget);
        
        if (budget.isNew()) {
            budgetMapper.insert(po);
            budget.setId(po.getId());
        } else {
            budgetMapper.update(po);
        }
        
        return budget;
    }
    
    @Override
    public Optional<Budget> findById(Long id) {
        com.atun.brain.infrastructure.persistence.mybatis.po.BudgetPO po = budgetMapper.findById(id);
        return Optional.ofNullable(budgetConverter.toDomain(po));
    }
    
    @Override
    public List<Budget> findByUserId(Long userId) {
        List<com.atun.brain.infrastructure.persistence.mybatis.po.BudgetPO> pos = budgetMapper.findByUserId(userId);
        return budgetConverter.toDomainList(pos);
    }
    
    @Override
    public List<Budget> findMonthlyBudgets(Long userId, int year, int month) {
        List<com.atun.brain.infrastructure.persistence.mybatis.po.BudgetPO> pos = 
            budgetMapper.findMonthlyBudgets(userId, year, month);
        return budgetConverter.toDomainList(pos);
    }
    
    @Override
    public List<Budget> findWeeklyBudgets(Long userId) {
        List<com.atun.brain.infrastructure.persistence.mybatis.po.BudgetPO> pos = budgetMapper.findWeeklyBudgets(userId);
        return budgetConverter.toDomainList(pos);
    }
    
    @Override
    public Optional<Budget> findByUserIdAndCategoryIdAndPeriod(
            Long userId, Long categoryId, String periodType, Integer year, Integer month) {
        com.atun.brain.infrastructure.persistence.mybatis.po.BudgetPO po = 
            budgetMapper.findByUserIdAndCategoryIdAndPeriod(userId, categoryId, periodType, year, month);
        return Optional.ofNullable(budgetConverter.toDomain(po));
    }
    
    @Override
    public void deleteById(Long id) {
        budgetMapper.deleteById(id);
    }
    
    @Override
    public Budget update(Budget budget) {
        return save(budget);
    }
}
