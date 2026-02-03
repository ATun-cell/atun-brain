package com.atun.brain.infrastructure.ai.prompt;

import com.atun.brain.infrastructure.ai.dto.AICategoryData;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类推荐提示模板
 *
 * @author lij
 * @date 2026/02/03
 */
public class CategoryClassifierPrompt {
    
    public static String build(String description, String type, List<AICategoryData> availableCategories) {
        String categoriesStr = availableCategories.stream()
                .filter(c -> c.getType().equals(type))
                .map(c -> String.format("  - ID: %d, 名称: %s, 父级: %s", 
                        c.getId(), c.getName(), 
                        c.getParentId() != null ? c.getParentId().toString() : "顶级"))
                .collect(Collectors.joining("\n"));
        
        return String.format("""
                请为以下交易选择最合适的分类：
                
                交易描述："%s"
                交易类型：%s
                
                可用分类列表：
                %s
                
                请返回JSON格式（不要添加任何其他文字）：
                {
                  "categoryId": 最匹配的分类ID（数字）,
                  "reason": "选择理由（简短）"
                }
                
                优先选择更具体的子分类（如有）。
                """, description, type.equals("EXPENSE") ? "支出" : "收入", categoriesStr);
    }
}
