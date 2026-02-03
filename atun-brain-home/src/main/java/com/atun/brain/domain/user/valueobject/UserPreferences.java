package com.atun.brain.domain.user.valueobject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户偏好设置值对象
 * 封装用户的个性化设置
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferences {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /** 首选语言 */
    @Builder.Default
    private String language = "zh-CN";
    
    /** 时区 */
    @Builder.Default
    private String timezone = "Asia/Shanghai";
    
    /** 货币类型 */
    @Builder.Default
    private String currency = "CNY";
    
    /** 是否启用通知 */
    @Builder.Default
    private boolean notificationsEnabled = true;
    
    /** 是否启用邮件通知 */
    @Builder.Default
    private boolean emailNotificationsEnabled = false;
    
    /** 默认代理类型 */
    @Builder.Default
    private String defaultAgentType = "BOOKKEEPING";
    
    /** 预算警告阈值（0-100） */
    @Builder.Default
    private int budgetWarningThreshold = 80;
    
    /** 是否自动生成报告 */
    @Builder.Default
    private boolean autoGenerateReports = true;
    
    /** 报告生成频率（天） */
    @Builder.Default
    private int reportGenerationInterval = 7;
    
    /** 扩展配置（用于存储自定义配置） */
    @Builder.Default
    private Map<String, Object> extensionConfig = new HashMap<>();
    
    /**
     * 创建默认偏好设置
     */
    public static UserPreferences createDefault() {
        return UserPreferences.builder().build();
    }
    
    /**
     * 从 JSON 字符串创建
     */
    public static UserPreferences fromJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return createDefault();
        }
        try {
            return objectMapper.readValue(json, UserPreferences.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("无法解析用户偏好设置: " + e.getMessage(), e);
        }
    }
    
    /**
     * 转换为 JSON 字符串
     */
    public String toJson() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("无法序列化用户偏好设置: " + e.getMessage(), e);
        }
    }
    
    /**
     * 设置扩展配置
     */
    public void setExtensionConfigValue(String key, Object value) {
        if (extensionConfig == null) {
            extensionConfig = new HashMap<>();
        }
        extensionConfig.put(key, value);
    }
    
    /**
     * 获取扩展配置
     */
    public Object getExtensionConfigValue(String key) {
        if (extensionConfig == null) {
            return null;
        }
        return extensionConfig.get(key);
    }
    
    /**
     * 验证预算警告阈值
     */
    public void setBudgetWarningThreshold(int threshold) {
        if (threshold < 0 || threshold > 100) {
            throw new IllegalArgumentException("预算警告阈值必须在 0-100 之间");
        }
        this.budgetWarningThreshold = threshold;
    }
    
    /**
     * 验证报告生成间隔
     */
    public void setReportGenerationInterval(int interval) {
        if (interval < 1) {
            throw new IllegalArgumentException("报告生成间隔必须大于 0");
        }
        this.reportGenerationInterval = interval;
    }
}
