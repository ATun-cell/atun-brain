package com.atun.brain.application.service.impl;

import com.atun.brain.application.dto.CategorySuggestionDTO;
import com.atun.brain.application.dto.TransactionDTO;
import com.atun.brain.application.dto.TransactionParseResultDTO;
import com.atun.brain.application.service.TransactionApplicationService;
import com.atun.brain.domain.finance.entity.Category;
import com.atun.brain.domain.finance.repository.CategoryRepository;
import com.atun.brain.infrastructure.ai.OpenAIAdapter;
import com.atun.brain.infrastructure.ai.dto.AICategoryData;
import com.atun.brain.infrastructure.ai.dto.AICategorySuggestion;
import com.atun.brain.infrastructure.ai.dto.AITransactionParseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 交易应用服务实现
 * 展示如何进行Domain ↔ Infrastructure DTO ↔ Application DTO转换
 *
 * @author lij
 * @date 2026/02/03
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionApplicationServiceImpl implements TransactionApplicationService {
    
    private final OpenAIAdapter openAIAdapter;
    private final CategoryRepository categoryRepository;
    
    @Override
    public TransactionParseResultDTO parseTransactionFromNaturalLanguage(String userMessage, Long userId) {
        log.info("解析交易自然语言输入，用户ID: {}, 输入: {}", userId, userMessage);
        
        // 1. 从领域层获取分类列表
        List<Category> domainCategories = categoryRepository.findAll();
        
        // 2. 将Domain Entity转换为Infrastructure DTO (Domain → Infrastructure DTO)
        List<AICategoryData> categoryDTOs = domainCategories.stream()
                .map(this::convertToAICategoryData)
                .collect(Collectors.toList());
        
        // 3. 调用基础设施层（只使用Infrastructure DTO）
        AITransactionParseResult infraResult = openAIAdapter.parseTransaction(userMessage, categoryDTOs);
        
        // 4. 将Infrastructure DTO转换为Application DTO (Infrastructure DTO → Application DTO)
        TransactionParseResultDTO result = convertToTransactionParseResultDTO(infraResult, domainCategories);
        
        log.info("交易解析完成: {}", result);
        return result;
    }
    
    @Override
    public CategorySuggestionDTO suggestCategory(String description, String type, Long userId) {
        log.info("AI推荐分类，描述: {}, 类型: {}, 用户ID: {}", description, type, userId);
        
        // 1. 从领域层获取分类列表
        List<Category> domainCategories = categoryRepository.findAll();
        
        // 2. Domain → Infrastructure DTO转换
        List<AICategoryData> categoryDTOs = domainCategories.stream()
                .map(this::convertToAICategoryData)
                .collect(Collectors.toList());
        
        // 3. 调用AI服务（基础设施层）
        AICategorySuggestion infraSuggestion = openAIAdapter.suggestCategory(description, type, categoryDTOs);
        
        // 4. Infrastructure DTO → Application DTO转换
        CategorySuggestionDTO result = convertToCategorySuggestionDTO(infraSuggestion, domainCategories);
        
        log.info("分类推荐完成: {}", result);
        return result;
    }
    
    @Override
    public TransactionDTO createTransactionFromInput(String userMessage, Long userId) {
        // TODO: 完整实现
        // 1. 解析自然语言
        // 2. 创建领域对象
        // 3. 持久化
        // 4. 转换为Application DTO返回
        throw new UnsupportedOperationException("待实现");
    }
    
    // ========== 转换方法 ==========
    
    /**
     * Domain Entity → Infrastructure DTO转换
     * 关键：基础设施层只需要必要的字段，不是完整的领域模型
     */
    private AICategoryData convertToAICategoryData(Category category) {
        AICategoryData dto = new AICategoryData();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setType(category.getType());
        dto.setParentId(category.getParentId());
        return dto;
    }
    
    /**
     * Infrastructure DTO → Application DTO转换
     * 将AI解析结果转换为应用层DTO
     */
    private TransactionParseResultDTO convertToTransactionParseResultDTO(
            AITransactionParseResult infraResult, List<Category> categories) {
        
        // 查找分类名称
        String categoryName = null;
        if (infraResult.getCategoryId() != null) {
            categoryName = categories.stream()
                    .filter(c -> c.getId().equals(infraResult.getCategoryId()))
                    .findFirst()
                    .map(Category::getName)
                    .orElse(null);
        }
        
        return TransactionParseResultDTO.builder()
                .amount(infraResult.getAmount() != null ? BigDecimal.valueOf(infraResult.getAmount()) : null)
                .type(infraResult.getType())
                .categoryId(infraResult.getCategoryId())
                .categoryName(categoryName)
                .description(infraResult.getDescription())
                .tags(infraResult.getTags())
                .transactionDate(infraResult.getTransactionDate())
                .build();
    }
    
    /**
     * Infrastructure DTO → Application DTO转换
     * 将AI分类推荐结果转换为应用层DTO
     */
    private CategorySuggestionDTO convertToCategorySuggestionDTO(
            AICategorySuggestion infraSuggestion, List<Category> categories) {
        
        // 查找分类名称
        String categoryName = null;
        if (infraSuggestion.getCategoryId() != null) {
            categoryName = categories.stream()
                    .filter(c -> c.getId().equals(infraSuggestion.getCategoryId()))
                    .findFirst()
                    .map(Category::getName)
                    .orElse(null);
        }
        
        return CategorySuggestionDTO.builder()
                .categoryId(infraSuggestion.getCategoryId())
                .categoryName(categoryName)
                .reason(infraSuggestion.getReason())
                .confidence(0.85) // 默认置信度，后续可以从模型获取
                .build();
    }
}
