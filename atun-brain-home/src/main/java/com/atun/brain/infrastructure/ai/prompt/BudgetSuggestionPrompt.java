package com.atun.brain.infrastructure.ai.prompt;

import com.atun.brain.infrastructure.ai.dto.AIBudgetRequest;

import java.util.stream.Collectors;

/**
 * 预算建议提示模板
 *
 * @author lij
 * @date 2026/02/03
 */
public class BudgetSuggestionPrompt {
    
    public static String build(AIBudgetRequest request) {
        return String.format("""
                请为用户提供预算建议：
                
                历史数据月数：%d 个月
                
                各分类平均月支出：
                %s
                
                请提供以下内容：
                
                1. 总体预算建议
                   - 月度总预算额度（基于历史数据）
                   - 储蓄目标比例
                   - 预算分配原则（如50/30/20法则）
                
                2. 分类预算建议
                   - 为每个主要支出分类建议合理的预算额度
                   - 预算依据说明（基于%d个月历史数据）
                   - 调整建议（优化方向）
                
                3. 预算执行建议
                   - 如何控制各分类支出
                   - 预警阈值设置建议（如超过80%%提醒）
                   - 弹性调整空间
                
                4. 财务目标建议
                   - 短期目标（1-3个月）
                   - 中期目标（3-6个月）
                   - 储蓄计划
                
                请基于合理的个人财务管理原则，结合用户实际情况给出建议。
                """,
                request.getMonths(),
                formatCategoryExpenses(request),
                request.getMonths());
    }
    
    private static String formatCategoryExpenses(AIBudgetRequest request) {
        if (request.getAvgSpendingByCategory() == null || request.getAvgSpendingByCategory().isEmpty()) {
            return "  （暂无历史数据）";
        }
        
        // 按金额降序排列
        return request.getAvgSpendingByCategory().entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .map(entry -> String.format("  - %s：平均 %.2f 元/月", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));
    }
}
