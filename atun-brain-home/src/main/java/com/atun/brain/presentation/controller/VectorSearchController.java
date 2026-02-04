package com.atun.brain.presentation.controller;

import com.atun.brain.application.dto.SimilarTransactionDTO;
import com.atun.brain.application.service.VectorSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 向量搜索控制器
 *
 * @author lij
 * @date 2026/02/03
 */
@Slf4j
@RestController
@RequestMapping("/vector-search")
@RequiredArgsConstructor
public class VectorSearchController {
    
    private final VectorSearchService vectorSearchService;
    
    /**
     * 搜索相似的交易
     * 示例：GET /api/vector-search/transactions?userId=1&query=买咖啡&limit=5
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<SimilarTransactionDTO>> searchSimilarTransactions(
            @RequestParam("userId") Long userId,
            @RequestParam("query") String query,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        log.info("搜索相似交易请求: userId={}, query={}, limit={}", userId, query, limit);
        
        List<SimilarTransactionDTO> results = 
            vectorSearchService.searchSimilarTransactions(query, userId, limit);
        
        return ResponseEntity.ok(results);
    }
    
    /**
     * 测试：手动存储一条交易向量
     * 示例：POST /api/vector-search/test-store
     */
    @PostMapping("/test-store")
    public ResponseEntity<String> testStoreVector(
            @RequestParam("transactionId") Long transactionId,
            @RequestParam("description") String description,
            @RequestParam("userId") Long userId) {
        log.info("测试存储向量: transactionId={}, description={}", transactionId, description);
        
        String vectorId = vectorSearchService.storeTransactionVector(transactionId, description, userId);
        
        return ResponseEntity.ok(vectorId);
    }
}
