package com.atun.brain.infrastructure.ai.prompt;

import com.atun.brain.infrastructure.ai.dto.AICategoryData;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 交易解析提示模板
 *
 * @author lij
 * @date 2026/02/03
 */
public class TransactionParserPrompt {
    
    public static String build(String userMessage, List<AICategoryData> availableCategories) {
        String categoriesStr = availableCategories.stream()
                .map(c -> String.format("  - ID: %d, 名称: %s, 类型: %s", 
                        c.getId(), c.getName(), c.getType()))
                .collect(Collectors.joining("\n"));
        
        return String.format("""
                请从以下用户输入中提取交易信息，并以JSON格式返回：
                
                用户输入："%s"
                
                可用分类列表：
                %s
                
                请严格按照以下JSON格式返回（不要添加任何其他文字）：
                {
                  "amount": 实际金额（数字）,
                  "type": "INCOME" 或 "EXPENSE",
                  "categoryId": 最匹配的分类ID（数字）,
                  "description": "简洁的交易描述",
                  "tags": ["标签1", "标签2"],
                  "transactionDate": "YYYY-MM-DD"
                }
                
                提取规则：
                1. 金额识别：支持67元、两块、三十五块等格式，将数字提取为小数格式（如36.5）
                2. 如果没有明确金额，返回null
                3. 根据关键词判断类型：花了/买了/支出->“EXPENSE”, 收入/赚了/发工资->“INCOME”
                4. 选择最匹配的分类ID
                5. 提取相关标签（如"早餐"、"地铁"、"星巴克"等）
                
                """, userMessage, categoriesStr);
    }
}
