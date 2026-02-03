package com.atun.brain.infrastructure.ai;

import com.atun.brain.infrastructure.ai.dto.*;
import com.atun.brain.infrastructure.ai.prompt.*;
import com.atun.brain.infrastructure.ai.util.ChineseNumberConverter;
import com.atun.brain.infrastructure.config.AIProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * OpenAI适配器
 *
 * @author lij
 * @date 2026/02/03
 */
@Slf4j
@Component
public class OpenAIAdapter {
    
    @Resource(name = "chatModel")
    private ChatModel chatModel;
    
    @Resource(name = "analysisModel")
    private ChatModel analysisModel;
    
    @Resource(name = "reportModel")
    private ChatModel reportModel;
    
    @Resource(name = "embeddingModel")
    private EmbeddingModel embeddingModel;
    
    @Resource
    private AIProperties aiProperties;
    @Resource
    private ObjectMapper objectMapper;
    
    /**
     * 对话接口（普通聊天）
     */
    public String chat(String userMessage) {
        log.info("Chat request: {}", userMessage);
        
        SystemMessage systemMessage = SystemMessage.from(aiProperties.getPrompts().getSystemRole());
        UserMessage userMsg = UserMessage.from(userMessage);
        
        ChatResponse response = chatModel.chat(systemMessage, userMsg);
        String answer = response.aiMessage().text();
        
        log.info("Chat response: {}", answer);
        return answer;
    }
    
    /**
     * 从自然语言中提取交易信息
     * @param userMessage 用户输入
     * @param availableCategories 可用分类列表（简单DTO）
     * @return 解析结果
     */
    public AITransactionParseResult parseTransaction(String userMessage, List<AICategoryData> availableCategories) {
        // 预处理：将中文数字转换为阿拉伯数字（提高AI识别准确率）
        String processedMessage = ChineseNumberConverter
                .convertChineseAmount(userMessage);
        log.info("从语言中提取交易信息: {}", userMessage);
        String prompt = TransactionParserPrompt.build(processedMessage, availableCategories);
        
        SystemMessage systemMessage = SystemMessage.from(
            "你是一个交易信息提取专家。从用户的自然语言描述中准确提取交易金额、类型、分类和描述信息。"
        );
        UserMessage userMsg = UserMessage.from(prompt);
        
        ChatResponse response = chatModel.chat(systemMessage, userMsg);
        String jsonResult = response.aiMessage().text();
        
        try {
            AITransactionParseResult result = objectMapper.readValue(jsonResult, AITransactionParseResult.class);
            // 日期由本地设置为当前日期（不依赖AI生成，AI模型不知道准确的当前日期）
            if (result != null) {
                result.setTransactionDate(java.time.LocalDate.now());
            }
            return result;
        } catch (Exception e) {
            log.error("Failed to parse transaction result: {}", jsonResult, e);
            return null;
        }
    }
    
    /**
     * 智能分类建议
     * @return 分类ID和推荐理由
     */
    public AICategorySuggestion suggestCategory(String description, String type, List<AICategoryData> availableCategories) {
        log.info("Suggesting category for: {}, type: {}", description, type);
        
        String prompt = CategoryClassifierPrompt.build(description, type, availableCategories);
        
        SystemMessage systemMessage = SystemMessage.from(
            "你是一个消费分类专家。根据交易描述和可用分类，选择最合适的分类。"
        );
        UserMessage userMsg = UserMessage.from(prompt);
        
        ChatResponse response = chatModel.chat(systemMessage, userMsg);
        String result = response.aiMessage().text();
        
        try {
            return objectMapper.readValue(result, AICategorySuggestion.class);
        } catch (Exception e) {
            log.error("Failed to parse category suggestion: {}", result, e);
            return null;
        }
    }
    
    /**
     * 分析交易模式
     * @param analysisRequest 分析请求（包含交易数据、时间范围等）
     * @return AI分析文本
     */
    public String analyzeTransactionPattern(AIAnalysisRequest analysisRequest) {
        log.info("Analyzing {} transactions from {} to {}", 
                analysisRequest.getTransactionCount(),
                analysisRequest.getStartDate(), 
                analysisRequest.getEndDate());
        
        String prompt = TransactionAnalysisPrompt.build(analysisRequest);
        
        SystemMessage systemMessage = SystemMessage.from(
            "你是一个专业的财务分析师。分析用户的消费模式，提供洞察和建议。"
        );
        UserMessage userMsg = UserMessage.from(prompt);
        
        ChatResponse response = analysisModel.chat(systemMessage, userMsg);
        return response.aiMessage().text();
    }
    
    /**
     * 生成财务报告
     * @param reportData 报告数据（DTO）
     * @return AI生成的报告文本
     */
    public String generateFinancialReport(AIReportData reportData) {
        log.info("Generating financial report, period: {} to {}", 
                reportData.getPeriodStart(), reportData.getPeriodEnd());
        
        String prompt = ReportGeneratorPrompt.build(reportData);
        
        SystemMessage systemMessage = SystemMessage.from(
            "你是一个专业的财务报告撰写专家。根据财务数据生成详细、易懂的分析报告。"
        );
        UserMessage userMsg = UserMessage.from(prompt);
        
        ChatResponse response = reportModel.chat(systemMessage, userMsg);
        return response.aiMessage().text();
    }
    
    /**
     * 预算建议
     * @param budgetRequest 预算建议请求（包含历史消费统计）
     * @return 各分类的预算建议
     */
    public Map<String, Double> suggestBudget(AIBudgetRequest budgetRequest) {
        log.info("Suggesting budget based on historical data");
        
        String prompt = BudgetSuggestionPrompt.build(budgetRequest);
        
        SystemMessage systemMessage = SystemMessage.from(
            "你是一个预算规划专家。根据用户的历史消费数据，提供合理的预算建议。"
        );
        UserMessage userMsg = UserMessage.from(prompt);
        
        ChatResponse response = analysisModel.chat(systemMessage, userMsg);
        String jsonResult = response.aiMessage().text();
        
        try {
            return objectMapper.readValue(jsonResult, Map.class);
        } catch (Exception e) {
            log.error("Failed to parse budget suggestion: {}", jsonResult, e);
            return Map.of();
        }
    }
    
    /**
     * 文本向量化（用于语义检索）
     */
    public List<Float> embed(String text) {
        log.debug("Embedding text: {}", text.substring(0, Math.min(50, text.length())));
        
        dev.langchain4j.data.embedding.Embedding embedding = embeddingModel.embed(text).content();
        return embedding.vectorAsList();
    }
}
