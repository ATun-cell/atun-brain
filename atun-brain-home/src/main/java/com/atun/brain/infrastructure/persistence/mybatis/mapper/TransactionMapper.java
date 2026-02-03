package com.atun.brain.infrastructure.persistence.mybatis.mapper;

import com.atun.brain.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TransactionMapper {
    
    Transaction findById(@Param("id") Long id);
    
    List<Transaction> findByUserId(@Param("userId") Long userId);
    
    List<Transaction> findByUserIdAndDateRange(@Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    List<Transaction> findByUserIdAndCategoryId(@Param("userId") Long userId,
            @Param("categoryId") Long categoryId);
    
    List<Transaction> findByUserIdWithCategory(@Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    int insert(Transaction transaction);
    
    int update(Transaction transaction);
    
    int deleteById(@Param("id") Long id);
}
