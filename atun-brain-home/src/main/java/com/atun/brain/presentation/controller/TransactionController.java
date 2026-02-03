package com.atun.brain.presentation.controller;

import com.atun.brain.application.dto.CategorySuggestionDTO;
import com.atun.brain.application.dto.TransactionParseResultDTO;
import com.atun.brain.application.service.TransactionApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 交易控制器
 *
 * @author lij
 * @date 2026/02/03
 */
@Slf4j
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    
    private final TransactionApplicationService transactionApplicationService;
    
    /**
     * AI解析交易（自然语言输入）
     * 示例：POST /api/transactions/parse?userId=1
     * Body: "今天早上在星巴克花了35元买咖啡"
     */
    @PostMapping("/parse")
    public ResponseEntity<TransactionParseResultDTO> parseTransaction(
            @RequestParam("userId") Long userId,
            @RequestBody String message) {
        log.info("收到交易解析请求，用户: {}, 消息: {}", userId, message);
        
        TransactionParseResultDTO result = transactionApplicationService
                .parseTransactionFromNaturalLanguage(message, userId);
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * AI推荐分类
     * 示例：GET /api/transactions/suggest-category?userId=1&description=买咖啡&type=EXPENSE
     */
    @GetMapping("/suggest-category")
    public ResponseEntity<CategorySuggestionDTO> suggestCategory(
            @RequestParam("userId") Long userId,
            @RequestParam("description") String description,
            @RequestParam("type") String type) {
        log.info("收到分类推荐请求，用户: {}, 描述: {}, 类型: {}", userId, description, type);
        
        CategorySuggestionDTO suggestion = transactionApplicationService
                .suggestCategory(description, type, userId);
        
        return ResponseEntity.ok(suggestion);
    }
}

