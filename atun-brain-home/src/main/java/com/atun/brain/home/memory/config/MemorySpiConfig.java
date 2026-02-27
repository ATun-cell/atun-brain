package com.atun.brain.home.memory.config;

import com.atun.brain.agent.memory.spi.MemoryWindowConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Memory 模块 Spring 配置
 *
 * @author atun-brain
 * @since 1.0
 */
@Configuration
public class MemorySpiConfig {

    @Bean
    @ConfigurationProperties(prefix = "agent.memory.window")
    public MemoryWindowProperties memoryWindowProperties() {
        return new MemoryWindowProperties();
    }

    @Bean
    public MemoryWindowConfig memoryWindowConfig(MemoryWindowProperties properties) {
        // 从配置文件读取配置
        MemoryWindowConfig.WindowStrategy strategy =
            "TOKEN".equals(properties.getStrategy())
                ? MemoryWindowConfig.WindowStrategy.TOKEN
                : MemoryWindowConfig.WindowStrategy.MESSAGE;

        return new MemoryWindowConfig(
                strategy,
                properties.getMaxMessages(),
                properties.getMaxTokens()
        );
    }

    /**
     * 记忆窗口配置属性类
     */
    @Data
    public static class MemoryWindowProperties {
        private String strategy = "MESSAGE";
        private int maxMessages = 100;
        private int maxTokens = 2000;
    }
}
