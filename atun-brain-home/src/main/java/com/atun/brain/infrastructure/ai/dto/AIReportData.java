package com.atun.brain.infrastructure.ai.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

/**
 * AI报告数据
 * 
 * @author lij
 * @date 2026/02/03
 */
@Data
@Builder
public class AIReportData {
    
    /** 报告类型 */
    private String reportType;  // WEEKLY | MONTHLY
    
    /** 句号开始 */
    private LocalDate periodStart;
    
    /** 句号结束 */
    private LocalDate periodEnd;
    
    /** 总费用 */
    private Double totalExpense;
    
    /** 总收入 */
    private Double totalIncome;
    
    /** 净金额 */
    private Double netAmount;
    
    /** 类别统计 */
    private Map<String, Double> categoryStatistics;  // 分类名->金额
}
