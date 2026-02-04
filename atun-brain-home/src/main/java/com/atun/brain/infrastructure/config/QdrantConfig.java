package com.atun.brain.infrastructure.config;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Qdrant 向量数据库配置
 *
 * @author lij
 * @date 2026/02/03
 */
@Configuration
public class QdrantConfig {
    
    @Bean
    public QdrantClient qdrantClient(QdrantProperties qdrantProperties) {
        return new QdrantClient(
            QdrantGrpcClient.newBuilder(
                qdrantProperties.getHost(),
                qdrantProperties.getPort(),
                qdrantProperties.isUseTls()
            ).build()
        );
    }
    
    /**
     * Qdrant 配置属性
     */
    @Data
    @Component
    @ConfigurationProperties(prefix = "qdrant")
    public static class QdrantProperties {
        /** 主机地址 */
        private String host = "localhost";
        
        /** 端口 */
        private Integer port = 6334;
        
        /** 是否使用TLS */
        private boolean useTls = false;
        
        /** 集合名称 */
        private String collectionName = "transactions";
        
        /** 向量维度 */
        private Integer vectorSize = 1536;
    }
}
