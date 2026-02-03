package com.atun.brain.infrastructure.ai.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * AI预算建议请求数据
 * 
 * @author lij
 * @date 2026/02/03
 */
@Data
@Builder
public class AIBudgetRequest {
    
    /** 月份 */
    private Integer months;  // 历史数据月数
    
    /** 按类别平均支出 */
    private Map<String, Double> avgSpendingByCategory;  // 分类->平均月消费
}
