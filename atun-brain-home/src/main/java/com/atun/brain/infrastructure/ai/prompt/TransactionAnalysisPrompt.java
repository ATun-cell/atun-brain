package com.atun.brain.infrastructure.ai.prompt;

import com.atun.brain.infrastructure.ai.dto.AIAnalysisRequest;

import java.util.stream.Collectors;

/**
 * 交易分析提示模板
 *
 * @author lij
 * @date 2026/02/03
 */
public class TransactionAnalysisPrompt {
    
    public static String build(AIAnalysisRequest request) {
        return String.format("""
                请分析以下用户的财务数据：
                
                分析周期：%s 至 %s
                交易总笔数：%d
                
                收支统计：
                - 总收入：%.2f 元
                - 总支出：%.2f 元
                - 净收入：%.2f 元
                - 储蓄率：%.1f%%
                
                分类支出统计：
                %s
                
                请提供以下内容：
                1. 消费模式分析（主要消费领域、习惯特点）
                2. 财务健康度评估（收支比、储蓄率）
                3. 异常消费检测（是否有不合理的大额支出）
                4. 优化建议（3-5条具体可行的改进建议）
                
                请以清晰的结构化格式返回，使用段落和列表。
                """, 
                request.getStartDate(),
                request.getEndDate(),
                request.getTransactionCount(),
                request.getTotalIncome(),
                request.getTotalExpense(),
                request.getNetAmount(),
                request.getTotalIncome() > 0 ? (request.getNetAmount() / request.getTotalIncome() * 100) : 0,
                formatCategoryStatistics(request));
    }
    
    private static String formatCategoryStatistics(AIAnalysisRequest request) {
        if (request.getCategoryStatistics() == null || request.getCategoryStatistics().isEmpty()) {
            return "  （暂无分类数据）";
        }
        
        // 按金额降序排列，取TOP5
        return request.getCategoryStatistics().entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(5)
                .map(entry -> String.format("  - %s：%.2f 元", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));
    }
}
