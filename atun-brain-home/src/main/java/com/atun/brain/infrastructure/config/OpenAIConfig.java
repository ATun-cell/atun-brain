package com.atun.brain.infrastructure.config;


import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * OpenAI配置（支持阿里云百炼）
 *
 * @author lij
 * @date 2026/02/03
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class OpenAIConfig {
    
    private final AIProperties aiProperties;
    
    /**
     * 对话模型（用于日常对话）
     */
    @Bean("chatModel")
    public ChatModel chatModel() {
        AIProperties.ProviderConfig config = aiProperties.getCurrentProvider();
        String modelName = aiProperties.getModel("chat");
        
        log.info("初始化聊天模型: provider={}, model={}",
                aiProperties.getProvider(), modelName);
        
        return OpenAiChatModel.builder()
                .apiKey(config.getApiKey())
                .baseUrl(config.getBaseUrl())
                .modelName(modelName)
                .temperature(config.getTemperature())
                .maxTokens(config.getMaxTokens())
                .timeout(Duration.ofSeconds(config.getTimeout()))
                .maxRetries(aiProperties.getRetry().getMaxAttempts())
                .logRequests(true)
                .logResponses(true)
                .build();
    }
    
    /**
     * 分析模型（用于财务分析）
     */
    @Bean("analysisModel")
    public ChatModel analysisModel() {
        AIProperties.ProviderConfig config = aiProperties.getCurrentProvider();
        String modelName = aiProperties.getModel("analysis");
        
        log.info("初始化分析模型: provider={}, model={}",
                aiProperties.getProvider(), modelName);
        
        return OpenAiChatModel.builder()
                .apiKey(config.getApiKey())
                .baseUrl(config.getBaseUrl())
                .modelName(modelName)
                .temperature(config.getTemperature())
                .maxTokens(config.getMaxTokens())
                .timeout(Duration.ofSeconds(config.getTimeout()))
                .maxRetries(aiProperties.getRetry().getMaxAttempts())
                .logRequests(true)
                .logResponses(true)
                .build();
    }
    
    /**
     * 报告生成模型（用于生成财务报告）
     */
    @Bean("reportModel")
    public ChatModel reportModel() {
        AIProperties.ProviderConfig config = aiProperties.getCurrentProvider();
        String modelName = aiProperties.getModel("report");
        
        log.info("初始化报告生成模型: provider={}, model={}",
                aiProperties.getProvider(), modelName);
        
        return OpenAiChatModel.builder()
                .apiKey(config.getApiKey())
                .baseUrl(config.getBaseUrl())
                .modelName(modelName)
                .temperature(config.getTemperature())
                .maxTokens(config.getMaxTokens())
                .timeout(Duration.ofSeconds(config.getTimeout()))
                .maxRetries(aiProperties.getRetry().getMaxAttempts())
                .logRequests(true)
                .logResponses(true)
                .build();
    }
    
    /**
     * 向量化模型（用于文本嵌入）
     */
    @Bean("embeddingModel")
    public EmbeddingModel embeddingModel() {
        AIProperties.ProviderConfig config = aiProperties.getCurrentProvider();
        String modelName = aiProperties.getModel("embedding");
        
        log.info("初始化向量嵌入模型: provider={}, model={}",
                aiProperties.getProvider(), modelName);
        
        return OpenAiEmbeddingModel.builder()
                .apiKey(config.getApiKey())
                .baseUrl(config.getBaseUrl())
                .modelName(modelName)
                .timeout(Duration.ofSeconds(config.getTimeout()))
                .maxRetries(aiProperties.getRetry().getMaxAttempts())
                .logRequests(true)
                .logResponses(true)
                .build();
    }
    
}
