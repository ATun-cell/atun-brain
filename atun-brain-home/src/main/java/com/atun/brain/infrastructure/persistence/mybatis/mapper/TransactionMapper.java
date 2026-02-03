package com.atun.brain.infrastructure.persistence.mybatis.mapper;

import com.atun.brain.domain.finance.aggregate.Transaction;
import com.atun.brain.infrastructure.persistence.mybatis.po.TransactionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TransactionMapper {
    
    TransactionPO findById(@Param("id") Long id);
    
    List<TransactionPO> findByUserId(@Param("userId") Long userId);
    
    List<TransactionPO> findByUserIdAndDateRange(@Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    List<TransactionPO> findByUserIdAndCategoryId(@Param("userId") Long userId,
            @Param("categoryId") Long categoryId);
    
    List<TransactionPO> findByUserIdWithCategory(@Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    int insert(TransactionPO transaction);
    
    int update(TransactionPO transaction);
    
    int deleteById(@Param("id") Long id);
}
