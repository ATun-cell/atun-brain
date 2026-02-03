package com.atun.brain.domain.conversation.valueobject;

import lombok.Value;

import java.util.UUID;

/**
 * 会话ID值对象
 * 表示一个唯一的对话会话标识
 *
 * @author lij
 * @date 2026/02/03
 */
@Value
public class SessionId {
    
    String value;
    
    private SessionId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("会话ID不能为空");
        }
        this.value = value;
    }
    
    /**
     * 生成新的会话ID
     */
    public static SessionId generate() {
        return new SessionId(UUID.randomUUID().toString());
    }
    
    /**
     * 从字符串创建会话ID
     */
    public static SessionId from(String value) {
        return new SessionId(value);
    }
    
    /**
     * 验证会话ID格式是否有效
     */
    public static boolean isValid(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return value;
    }
}
