package com.atun.brain.application.service.impl;

import com.atun.brain.application.dto.SimilarTransactionDTO;
import com.atun.brain.application.service.VectorSearchService;
import com.atun.brain.infrastructure.ai.OpenAIAdapter;
import com.atun.brain.infrastructure.config.QdrantConfig;
import com.atun.brain.infrastructure.persistence.qdrant.VectorStoreAdapter;
import com.atun.brain.infrastructure.persistence.qdrant.dto.VectorSearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 向量搜索服务实现
 *
 * @author lij
 * @date 2026/02/03
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VectorSearchServiceImpl implements VectorSearchService {
    
    private final VectorStoreAdapter vectorStoreAdapter;
    private final OpenAIAdapter openAIAdapter;
    private final QdrantConfig.QdrantProperties qdrantProperties;
    
    @PostConstruct
    @Override
    public void initializeCollections() {
        log.info("初始化向量数据库集合...");
        
        String collectionName = qdrantProperties.getCollectionName();
        int vectorSize = qdrantProperties.getVectorSize();
        
        vectorStoreAdapter.initCollection(collectionName, vectorSize);
        
        log.info("向量数据库集合初始化完成: {}", collectionName);
    }
    
    @Override
    public String storeTransactionVector(Long transactionId, String description, Long userId) {
        log.info("存储交易向量: transactionId={}, description={}", transactionId, description);
        
        try {
            // 1. 生成向量ID
            String vectorId = UUID.randomUUID().toString();
            
            // 2. 使用 AI 将交易描述向量化
            List<Float> vector = openAIAdapter.embed(description);
            
            // 3. 构建元数据
            Map<String, Object> payload = new HashMap<>();
            payload.put("transactionId", transactionId);
            payload.put("userId", userId);
            payload.put("description", description);
            payload.put("timestamp", System.currentTimeMillis());
            
            // 4. 存储到向量数据库
            vectorStoreAdapter.storeVector(
                qdrantProperties.getCollectionName(),
                vectorId,
                vector,
                payload
            );
            
            log.info("交易向量存储成功: vectorId={}", vectorId);
            return vectorId;
            
        } catch (Exception e) {
            log.error("存储交易向量失败: transactionId={}", transactionId, e);
            throw new RuntimeException("存储交易向量失败", e);
        }
    }
    
    @Override
    public List<SimilarTransactionDTO> searchSimilarTransactions(String query, Long userId, int limit) {
        log.info("搜索相似交易: query={}, userId={}, limit={}", query, userId, limit);
        
        try {
            // 1. 向量化查询文本
            List<Float> queryVector = openAIAdapter.embed(query);
            
            // 2. 构建过滤条件（只搜索该用户的交易）
            Map<String, Object> filter = new HashMap<>();
            filter.put("userId", userId);
            
            // 3. 执行向量搜索
            List<VectorSearchResult> results = vectorStoreAdapter.searchSimilarWithFilter(
                qdrantProperties.getCollectionName(),
                queryVector,
                filter,
                limit
            );
            
            // 4. 转换为应用层 DTO
            List<SimilarTransactionDTO> similarTransactions = results.stream()
                .map(result -> SimilarTransactionDTO.builder()
                    .transactionId(((Number) result.getPayload().get("transactionId")).longValue())
                    .description((String) result.getPayload().get("description"))
                    .similarity(result.getScore())
                    .vectorId(result.getId())
                    .build())
                .collect(Collectors.toList());
            
            log.info("找到 {} 条相似交易", similarTransactions.size());
            return similarTransactions;
            
        } catch (Exception e) {
            log.error("搜索相似交易失败", e);
            throw new RuntimeException("搜索相似交易失败", e);
        }
    }
    
    @Override
    public void deleteTransactionVector(String vectorId) {
        log.info("删除交易向量: vectorId={}", vectorId);
        
        try {
            vectorStoreAdapter.deleteVector(
                qdrantProperties.getCollectionName(),
                vectorId
            );
            
            log.info("交易向量删除成功: vectorId={}", vectorId);
        } catch (Exception e) {
            log.error("删除交易向量失败: vectorId={}", vectorId, e);
            throw new RuntimeException("删除交易向量失败", e);
        }
    }
}
