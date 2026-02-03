package com.atun.brain.domain.finance.entity;

import com.atun.brain.domain.finance.valueobject.Money;
import com.atun.brain.domain.finance.valueobject.Period;
import com.atun.brain.domain.shared.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 生成报告实体
 * 存储定时生成的财务分析报告
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GeneratedReport extends BaseEntity {
    
    /** 用户ID */
    private Long userId;
    
    /** 报告周期 */
    private Period period;
    
    /** 报告类型（WEEKLY/MONTHLY） */
    private String reportType;
    
    /** 总支出 */
    private Money totalExpense;
    
    /** 总收入 */
    private Money totalIncome;
    
    /** 净值（收入-支出） */
    private Money netAmount;
    
    /** 分类统计（分类ID -> 金额） */
    private Map<Long, Money> categoryStatistics;
    
    /** AI生成的分析文本 */
    private String analysisText;
    
    /** 关联的向量ID（用于语义检索） */
    private String vectorId;
    
    /**
     * 创建周报
     */
    public static GeneratedReport createWeekly(Long userId, Period period) {
        GeneratedReport report = new GeneratedReport();
        report.setUserId(userId);
        report.setPeriod(period);
        report.setReportType("WEEKLY");
        return report;
    }
    
    /**
     * 创建月报
     */
    public static GeneratedReport createMonthly(Long userId, Period period) {
        GeneratedReport report = new GeneratedReport();
        report.setUserId(userId);
        report.setPeriod(period);
        report.setReportType("MONTHLY");
        return report;
    }
    
    /**
     * 设置财务数据
     */
    public void setFinancialData(Money totalExpense, Money totalIncome, 
                                 Map<Long, Money> categoryStatistics) {
        this.totalExpense = totalExpense;
        this.totalIncome = totalIncome;
        this.netAmount = totalIncome.subtract(totalExpense);
        this.categoryStatistics = categoryStatistics;
    }
    
    /**
     * 设置AI分析文本
     */
    public void setAiAnalysis(String analysisText, String vectorId) {
        this.analysisText = analysisText;
        this.vectorId = vectorId;
    }
    
    /**
     * 判断是否盈余
     */
    public boolean isSurplus() {
        return netAmount != null && netAmount.isPositive();
    }
    
    /**
     * 判断是否亏损
     */
    public boolean isDeficit() {
        return netAmount != null && netAmount.isNegative();
    }
}
