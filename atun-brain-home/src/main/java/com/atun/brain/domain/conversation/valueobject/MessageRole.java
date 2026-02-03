package com.atun.brain.domain.conversation.valueobject;

import lombok.Getter;

/**
 * 消息角色枚举
 * 定义对话中消息的发送者角色
 *
 * @author lij
 * @date 2026/02/03
 */
@Getter
public enum MessageRole {
    
    /** 用户消息 */
    USER("user", "用户"),
    
    /** AI助手消息 */
    ASSISTANT("assistant", "助手"),
    
    /** 系统消息（用于设置上下文、提示词等） */
    SYSTEM("system", "系统"),
    
    /** 函数调用结果 */
    FUNCTION("function", "函数");
    
    private final String code;
    private final String displayName;
    
    MessageRole(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
    
    /**
     * 从代码获取枚举
     */
    public static MessageRole fromCode(String code) {
        for (MessageRole role : values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("未知的消息角色: " + code);
    }
    
    /**
     * 判断是否为用户消息
     */
    public boolean isUser() {
        return this == USER;
    }
    
    /**
     * 判断是否为助手消息
     */
    public boolean isAssistant() {
        return this == ASSISTANT;
    }
}
