package com.atun.brain.infrastructure.persistence.qdrant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 向量搜索结果（基础设施层 DTO）
 * 
 * @author lij
 * @date 2026/02/03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VectorSearchResult {
    
    /** 向量ID */
    private String id;
    
    /** 相似度分数 */
    private Float score;
    
    /** 附加元数据 */
    private Map<String, Object> payload;
}
