package com.atun.brain.infrastructure.persistence.mybatis.mapper;

import com.atun.brain.infrastructure.persistence.mybatis.po.BudgetPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BudgetMapper {
    
    BudgetPO findById(@Param("id") Long id);
    
    List<BudgetPO> findByUserId(@Param("userId") Long userId);
    
    List<BudgetPO> findMonthlyBudgets(@Param("userId") Long userId, 
                                   @Param("year") int year, 
                                   @Param("month") int month);
    
    List<BudgetPO> findWeeklyBudgets(@Param("userId") Long userId);
    
    BudgetPO findByUserIdAndCategoryIdAndPeriod(@Param("userId") Long userId,
                                              @Param("categoryId") Long categoryId,
                                              @Param("periodType") String periodType,
                                              @Param("year") Integer year,
                                              @Param("month") Integer month);
    
    int insert(BudgetPO budget);
    
    int update(BudgetPO budget);
    
    int deleteById(@Param("id") Long id);
}
