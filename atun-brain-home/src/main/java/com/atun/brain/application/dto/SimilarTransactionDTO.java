package com.atun.brain.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 相似交易 DTO（应用层）
 * 
 * @author lij
 * @date 2026/02/03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimilarTransactionDTO {
    
    /** 交易ID */
    private Long transactionId;
    
    /** 交易描述 */
    private String description;
    
    /** 相似度（0-1，越高越相似） */
    private Float similarity;
    
    /** 向量ID */
    private String vectorId;
}
