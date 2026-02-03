package com.atun.brain.domain.conversation.valueobject;

import lombok.Getter;

/**
 * Agent类型枚举
 * 定义系统支持的不同AI代理类型
 *
 * @author lij
 * @date 2026/02/03
 */
@Getter
public enum AgentType {
    
    /** 记账代理 */
    BOOKKEEPING("BOOKKEEPING", "记账代理", "处理记账、交易相关的对话"),
    
    /** 知识管理代理（预留） */
    KNOWLEDGE("KNOWLEDGE", "知识管理代理", "管理个人知识库、笔记"),
    
    /** 决策辅助代理（预留） */
    DECISION("DECISION", "决策辅助代理", "提供决策建议和分析"),
    
    /** 通用对话代理 */
    GENERAL("GENERAL", "通用代理", "处理一般性对话");
    
    private final String code;
    private final String displayName;
    private final String description;
    
    AgentType(String code, String displayName, String description) {
        this.code = code;
        this.displayName = displayName;
        this.description = description;
    }
    
    /**
     * 从代码获取枚举
     */
    public static AgentType fromCode(String code) {
        for (AgentType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的Agent类型: " + code);
    }
    
    /**
     * 判断是否为记账代理
     */
    public boolean isBookkeeping() {
        return this == BOOKKEEPING;
    }
}
