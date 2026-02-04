package com.atun.brain.infrastructure.persistence.qdrant;

import com.atun.brain.infrastructure.persistence.qdrant.dto.VectorData;
import com.atun.brain.infrastructure.persistence.qdrant.dto.VectorSearchResult;

import java.util.List;
import java.util.Map;

/**
 * 向量存储适配器接口
 * 提供向量存储、检索和搜索功能
 *
 * @author lij
 * @date 2026/02/03
 */
public interface VectorStoreAdapter {
    
    /**
     * 初始化集合（如果不存在）
     * @param collectionName 集合名称
     * @param vectorSize 向量维度
     */
    void initCollection(String collectionName, int vectorSize);
    
    /**
     * 存储向量
     * @param collectionName 集合名称
     * @param id 向量ID（UUID字符串）
     * @param vector 向量数据
     * @param payload 附加元数据（如交易描述、分类等）
     */
    void storeVector(String collectionName, String id, List<Float> vector, Map<String, Object> payload);
    
    /**
     * 批量存储向量
     * @param collectionName 集合名称
     * @param vectors 向量列表（每个包含id、vector、payload）
     */
    void batchStoreVectors(String collectionName, List<VectorData> vectors);
    
    /**
     * 相似度搜索
     * @param collectionName 集合名称
     * @param queryVector 查询向量
     * @param limit 返回结果数量
     * @return 相似结果列表
     */
    List<VectorSearchResult> searchSimilar(String collectionName, List<Float> queryVector, int limit);
    
    /**
     * 带过滤条件的相似度搜索
     * @param collectionName 集合名称
     * @param queryVector 查询向量
     * @param filter 过滤条件（如只搜索特定用户的数据）
     * @param limit 返回结果数量
     * @return 相似结果列表
     */
    List<VectorSearchResult> searchSimilarWithFilter(String collectionName, List<Float> queryVector, 
                                                Map<String, Object> filter, int limit);
    
    /**
     * 根据ID获取向量
     * @param collectionName 集合名称
     * @param id 向量ID
     * @return 向量数据
     */
    VectorData getVector(String collectionName, String id);
    
    /**
     * 删除向量
     * @param collectionName 集合名称
     * @param id 向量ID
     */
    void deleteVector(String collectionName, String id);
    
    /**
     * 检查集合是否存在
     * @param collectionName 集合名称
     * @return 是否存在
     */
    boolean collectionExists(String collectionName);
}
