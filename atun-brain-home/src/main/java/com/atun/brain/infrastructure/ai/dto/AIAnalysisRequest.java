package com.atun.brain.infrastructure.ai.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * AI分析请求数据
 * 
 * @author lij
 * @date 2026/02/03
 */
@Data
@Builder
public class AIAnalysisRequest {
    
    /** 开工日期 */
    private LocalDate startDate;
    
    /** 结束日期 */
    private LocalDate endDate;
    
    /** 交易计数 */
    private Integer transactionCount;
    
    /** 总收入 */
    private Double totalIncome;
    
    /** 总费用 */
    private Double totalExpense;
    
    /** 净金额 */
    private Double netAmount;
    
    /** 类别统计 */
    private Map<String, Double> categoryStatistics;  // 分类名->金额
    
    /** 交易 */
    private List<AITransactionData> transactions;
}
