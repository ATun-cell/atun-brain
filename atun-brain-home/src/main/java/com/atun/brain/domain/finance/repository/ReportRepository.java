package com.atun.brain.domain.finance.repository;

import com.atun.brain.domain.finance.entity.GeneratedReport;
import com.atun.brain.domain.finance.valueobject.Period;

import java.util.List;
import java.util.Optional;

/**
 * 报告仓储接口 - 领域层
 * 定义生成报告的持久化操作
 *
 * @author lij
 * @date 2026/02/03
 */
public interface ReportRepository {
    
    /**
     * 保存报告
     */
    GeneratedReport save(GeneratedReport report);
    
    /**
     * 根据ID查找报告
     */
    Optional<GeneratedReport> findById(Long id);
    
    /**
     * 查找用户的所有报告
     */
    List<GeneratedReport> findByUserId(Long userId);
    
    /**
     * 查找用户的周报
     */
    List<GeneratedReport> findWeeklyReports(Long userId);
    
    /**
     * 查找用户的月报
     */
    List<GeneratedReport> findMonthlyReports(Long userId);
    
    /**
     * 查找特定周期的报告
     */
    Optional<GeneratedReport> findByUserIdAndPeriod(
        Long userId, String reportType, Period period);
    
    /**
     * 删除报告
     */
    void deleteById(Long id);
    
    /**
     * 更新报告
     */
    GeneratedReport update(GeneratedReport report);
}
