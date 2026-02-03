package com.atun.brain.application.service;

import com.atun.brain.application.dto.CategorySuggestionDTO;
import com.atun.brain.application.dto.TransactionDTO;
import com.atun.brain.application.dto.TransactionParseResultDTO;

/**
 * 交易应用服务
 *
 * @author lij
 * @date 2026/02/03
 */
public interface TransactionApplicationService {
    
    /**
     * 从自然语言解析交易信息
     * 
     * @param userMessage 用户输入的自然语言（如："今天早上吃早餐花了25元"）
     * @param userId 用户ID
     * @return 解析后的交易信息
     */
    TransactionParseResultDTO parseTransactionFromNaturalLanguage(String userMessage, Long userId);
    
    /**
     * AI推荐交易分类
     * 
     * @param description 交易描述
     * @param type 交易类型（INCOME/EXPENSE）
     * @param userId 用户ID
     * @return 推荐的分类及理由
     */
    CategorySuggestionDTO suggestCategory(String description, String type, Long userId);
    
    /**
     * 创建交易（支持自然语言输入）
     * 
     * @param userMessage 用户输入（可以是结构化数据或自然语言）
     * @param userId 用户ID
     * @return 创建的交易信息
     */
    TransactionDTO createTransactionFromInput(String userMessage, Long userId);
}

