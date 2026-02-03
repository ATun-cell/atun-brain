package com.atun.brain.infrastructure.ai.prompt;

import com.atun.brain.infrastructure.ai.dto.AIReportData;

import java.util.stream.Collectors;

/**
 * 财务报告生成提示模板
 *
 * @author lij
 * @date 2026/02/03
 */
public class ReportGeneratorPrompt {
    
    public static String build(AIReportData data) {
        return String.format("""
                请生成一份专业的个人财务报告：
                
                报告周期：%s 至 %s
                报告类型：%s
                
                数据概览：
                - 总收入：%.2f 元
                - 总支出：%.2f 元
                - 净收入：%.2f 元
                - 结余率：%.1f%%
                
                分类支出统计：
                %s
                
                请生成包含以下部分的报告：
                
                ## 一、财务概览
                - 本期收支情况总结
                - 收支平衡分析
                - 主要变化趋势
                
                ## 二、收入分析
                - 收入结构分析
                - 收入稳定性评估
                - 收入优化建议
                
                ## 三、支出分析
                - 支出结构分析（按分类）
                - 主要支出项评估
                - 异常支出检测
                - 节约潜力分析
                
                ## 四、财务健康度评分
                - 综合评分（0-100分）
                - 评分依据说明
                - 风险提示（如有）
                
                ## 五、优化建议
                - 3-5条具体可行的改进建议
                - 预期效果说明
                
                ## 六、下期规划
                - 预算建议
                - 财务目标建议
                
                请使用专业、客观的语言，提供数据支持的洞察。
                """,
                data.getPeriodStart(),
                data.getPeriodEnd(),
                formatReportType(data.getReportType()),
                data.getTotalIncome(),
                data.getTotalExpense(),
                data.getNetAmount(),
                data.getTotalIncome() > 0 ? (data.getNetAmount() / data.getTotalIncome() * 100) : 0,
                formatCategoryStatistics(data));
    }
    
    private static String formatReportType(String type) {
        if (type == null) {
            return "自定义周期报告";
        }
        return switch (type) {
            case "WEEKLY" -> "周度报告";
            case "MONTHLY" -> "月度报告";
            case "QUARTERLY" -> "季度报告";
            case "YEARLY" -> "年度报告";
            default -> "自定义周期报告";
        };
    }
    
    private static String formatCategoryStatistics(AIReportData data) {
        if (data.getCategoryStatistics() == null || data.getCategoryStatistics().isEmpty()) {
            return "  （暂无分类数据）";
        }
        
        // 按金额降序排列
        return data.getCategoryStatistics().entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .map(entry -> String.format("  - %s：%.2f 元", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));
    }
}
