package com.atun.brain.infrastructure.persistence.mybatis.impl;

import com.atun.brain.domain.finance.entity.GeneratedReport;
import com.atun.brain.domain.finance.repository.ReportRepository;
import com.atun.brain.domain.finance.valueobject.Period;
import com.atun.brain.infrastructure.persistence.mybatis.converter.GeneratedReportConverter;
import com.atun.brain.infrastructure.persistence.mybatis.mapper.GeneratedReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 生成报告仓储实现类 - 基础设施层
 * 使用MyBatis进行数据持久化
 *
 * @author lij
 * @date 2026/02/03
 */
@Repository
@RequiredArgsConstructor
public class GenerateReportRepositoryImpl implements ReportRepository {
    
    private final GeneratedReportMapper reportMapper;
    private final GeneratedReportConverter reportConverter;
    
    @Override
    public GeneratedReport save(GeneratedReport report) {
        com.atun.brain.infrastructure.persistence.mybatis.po.GenerateReportPO po = reportConverter.toPO(report);
        
        if (report.isNew()) {
            reportMapper.insert(po);
            report.setId(po.getId());
        } else {
            reportMapper.update(po);
        }
        
        return report;
    }
    
    @Override
    public Optional<GeneratedReport> findById(Long id) {
        com.atun.brain.infrastructure.persistence.mybatis.po.GenerateReportPO po = reportMapper.findById(id);
        return Optional.ofNullable(reportConverter.toDomain(po));
    }
    
    @Override
    public List<GeneratedReport> findByUserId(Long userId) {
        List<com.atun.brain.infrastructure.persistence.mybatis.po.GenerateReportPO> pos = reportMapper.findByUserId(userId);
        return reportConverter.toDomainList(pos);
    }
    
    @Override
    public List<GeneratedReport> findWeeklyReports(Long userId) {
        List<com.atun.brain.infrastructure.persistence.mybatis.po.GenerateReportPO> pos = reportMapper.findWeeklyReports(userId);
        return reportConverter.toDomainList(pos);
    }
    
    @Override
    public List<GeneratedReport> findMonthlyReports(Long userId) {
        List<com.atun.brain.infrastructure.persistence.mybatis.po.GenerateReportPO> pos = reportMapper.findMonthlyReports(userId);
        return reportConverter.toDomainList(pos);
    }
    
    @Override
    public Optional<GeneratedReport> findByUserIdAndPeriod(
            Long userId, String reportType, Period period) {
        com.atun.brain.infrastructure.persistence.mybatis.po.GenerateReportPO po = 
            reportMapper.findByUserIdAndPeriod(userId, period.getStartDate(), period.getEndDate());
        return Optional.ofNullable(reportConverter.toDomain(po));
    }
    
    @Override
    public void deleteById(Long id) {
        reportMapper.deleteById(id);
    }
    
    @Override
    public GeneratedReport update(GeneratedReport report) {
        return save(report);
    }
}
