package com.atun.brain.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * AI配置属性
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
@Component
@ConfigurationProperties(prefix = "ai")
public class AIProperties {
    /**
     * AI服务提供商：dashscope | openai | custom
     */
    private String provider = "dashscope";
    
    /**
     * 阿里云百炼配置
     */
    private ProviderConfig dashscope = new ProviderConfig();
    
    /**
     * OpenAI配置
     */
    private ProviderConfig openai = new ProviderConfig();
    
    /**
     * 重试配置
     */
    private RetryConfig retry = new RetryConfig();
    
    /**
     * Prompt配置
     */
    private PromptConfig prompts = new PromptConfig();
    
    @Data
    public static class ProviderConfig {
        private String apiKey;
        private String baseUrl;
        private Map<String, String> models;
        private Double temperature = 0.7;
        private Integer maxTokens = 2000;
        private Integer timeout = 60;
    }
    
    @Data
    public static class RetryConfig {
        private Integer maxAttempts = 3;
        private Long backoffMs = 1000L;
    }
    
    @Data
    public static class PromptConfig {
        private String systemRole = "你是一个专业的AI财务助手，帮助用户管理个人财务、记账和分析消费习惯。";
        private Boolean enableFunctionCall = true;
    }
    
    /**
     * 获取当前提供商配置
     */
    public ProviderConfig getCurrentProvider() {
        return switch (provider.toLowerCase()) {
            case "openai" -> openai;
            case "dashscope" -> dashscope;
            default -> dashscope;
        };
    }
    
    /**
     * 获取指定场景的模型名称
     */
    public String getModel(String scenario) {
        ProviderConfig config = getCurrentProvider();
        if (config.getModels() == null || !config.getModels().containsKey(scenario)) {
            return config.getModels() != null ? config.getModels().get("default") : "qwen-turbo";
        }
        return config.getModels().get(scenario);
    }
}
