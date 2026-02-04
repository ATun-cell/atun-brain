package com.atun.brain.infrastructure.persistence.qdrant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 向量数据（基础设施层 DTO）
 * 
 * @author lij
 * @date 2026/02/03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VectorData {
    
    /** 向量ID */
    private String id;
    
    /** 向量数据 */
    private List<Float> vector;
    
    /** 附加元数据 */
    private Map<String, Object> payload;
}
