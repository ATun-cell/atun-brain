package com.atun.brain.infrastructure.persistence.mybatis.mapper;

import com.atun.brain.infrastructure.persistence.mybatis.po.GenerateReportPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface GeneratedReportMapper {
    
    GenerateReportPO findById(@Param("id") Long id);
    
    List<GenerateReportPO> findByUserId(@Param("userId") Long userId);
    
    List<GenerateReportPO> findWeeklyReports(@Param("userId") Long userId);
    
    List<GenerateReportPO> findMonthlyReports(@Param("userId") Long userId);
    
    GenerateReportPO findByUserIdAndPeriod(@Param("userId") Long userId,
                                         @Param("periodStart") LocalDate periodStart,
                                         @Param("periodEnd") LocalDate periodEnd);
    
    int insert(GenerateReportPO report);
    
    int update(GenerateReportPO report);
    
    int deleteById(@Param("id") Long id);
}
