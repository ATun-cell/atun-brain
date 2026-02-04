package com.atun.brain.application.service;

import com.atun.brain.application.dto.SimilarTransactionDTO;

import java.util.List;

/**
 * 向量搜索应用服务
 * 提供基于向量的语义搜索功能
 *
 * @author lij
 * @date 2026/02/03
 */
public interface VectorSearchService {
    
    /**
     * 初始化向量集合
     */
    void initializeCollections();
    
    /**
     * 将交易存储为向量
     * @param transactionId 交易ID
     * @param description 交易描述
     * @param userId 用户ID
     * @return 向量ID
     */
    String storeTransactionVector(Long transactionId, String description, Long userId);
    
    /**
     * 搜索相似的交易
     * @param query 查询文本
     * @param userId 用户ID（只搜索该用户的交易）
     * @param limit 返回数量
     * @return 相似交易DTO列表
     */
    List<SimilarTransactionDTO> searchSimilarTransactions(String query, Long userId, int limit);
    
    /**
     * 删除交易向量
     * @param vectorId 向量ID
     */
    void deleteTransactionVector(String vectorId);
}
