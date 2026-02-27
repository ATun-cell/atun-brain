package com.atun.brain.home.rag.config;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RAG 模块 Spring 配置
 *
 * @author atun-brain
 * @since 1.0
 */
@Configuration
public class RagSpiConfig {

    @Bean
    public QdrantClient qdrantClient(@Value("${agent.rag.qdrant.host:localhost}") String host,
                                     @Value("${agent.rag.qdrant.port:6334}") int port) {
        return new QdrantClient(
                QdrantGrpcClient.newBuilder(host, port, false).build());
    }

    @Bean
    @ConfigurationProperties(prefix = "agent.rag.qdrant")
    public QdrantProperties qdrantProperties() {
        return new QdrantProperties();
    }

    /**
     * Qdrant 配置属性类
     */
    @Data
    public static class QdrantProperties {
        private String host = "localhost";
        private int port = 6334;
        private String collection = "atun-brain";
    }
}
