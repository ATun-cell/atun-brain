package com.atun.brain.infrastructure.persistence.mybatis.converter;

import com.atun.brain.domain.finance.entity.GeneratedReport;
import com.atun.brain.domain.finance.valueobject.Money;
import com.atun.brain.domain.finance.valueobject.Period;
import com.atun.brain.infrastructure.persistence.mybatis.po.GenerateReportPO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 生成报告实体与PO之间的转换器
 *
 * @author lij
 * @date 2026/02/03
 */
@Component
public class GeneratedReportConverter {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 领域模型 -> PO（用于持久化）
     */
    public GenerateReportPO toPO(GeneratedReport domain) {
        if (domain == null) {
            return null;
        }
        
        GenerateReportPO po = new GenerateReportPO();
        po.setId(domain.getId());
        po.setUserId(domain.getUserId());
        
        if (domain.getPeriod() != null) {
            po.setPeriodStart(domain.getPeriod().getStartDate());
            po.setPeriodEnd(domain.getPeriod().getEndDate());
        }
        
        po.setReportType(domain.getReportType());
        po.setTotalExpense(domain.getTotalExpense() != null ? domain.getTotalExpense().getAmount() : null);
        po.setTotalIncome(domain.getTotalIncome() != null ? domain.getTotalIncome().getAmount() : null);
        po.setNetAmount(domain.getNetAmount() != null ? domain.getNetAmount().getAmount() : null);
        
        // 将categoryStatistics转换为JSON
        if (domain.getCategoryStatistics() != null) {
            Map<String, Object> categoryStats = new HashMap<>();
            domain.getCategoryStatistics().forEach((categoryId, money) -> 
                categoryStats.put(categoryId.toString(), money.getAmount())
            );
            po.setCategoryStatistics(convertToJson(categoryStats));
        }
        
        po.setAnalysisText(domain.getAnalysisText());
        po.setVectorId(domain.getVectorId());
        po.setCreatedAt(domain.getCreatedAt());
        po.setUpdatedAt(domain.getUpdatedAt());
        
        return po;
    }
    
    /**
     * PO -> 领域模型（从数据库加载）
     */
    public GeneratedReport toDomain(GenerateReportPO po) {
        if (po == null) {
            return null;
        }
        
        GeneratedReport domain = new GeneratedReport();
        domain.setId(po.getId());
        domain.setUserId(po.getUserId());
        
        if (po.getPeriodStart() != null && po.getPeriodEnd() != null) {
            domain.setPeriod(new Period(po.getPeriodStart(), po.getPeriodEnd()));
        }
        
        domain.setReportType(po.getReportType());
        domain.setTotalExpense(po.getTotalExpense() != null ? Money.cny(po.getTotalExpense()) : null);
        domain.setTotalIncome(po.getTotalIncome() != null ? Money.cny(po.getTotalIncome()) : null);
        domain.setNetAmount(po.getNetAmount() != null ? Money.cny(po.getNetAmount()) : null);
        
        // 从JSON转换categoryStatistics
        if (po.getCategoryStatistics() != null && !po.getCategoryStatistics().isEmpty()) {
            Map<String, Object> categoryStats = convertFromJson(po.getCategoryStatistics());
            Map<Long, Money> domainCategoryStats = new HashMap<>();
            categoryStats.forEach((categoryId, amount) -> 
                domainCategoryStats.put(Long.valueOf(categoryId), convertToMoney(amount))
            );
            domain.setCategoryStatistics(domainCategoryStats);
        }
        
        domain.setAnalysisText(po.getAnalysisText());
        domain.setVectorId(po.getVectorId());
        domain.setCreatedAt(po.getCreatedAt());
        domain.setUpdatedAt(po.getUpdatedAt());
        
        return domain;
    }
    
    /**
     * PO列表 -> 领域模型列表
     */
    public List<GeneratedReport> toDomainList(List<GenerateReportPO> pos) {
        if (pos == null) {
            return new ArrayList<>();
        }
        return pos.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * 对象转JSON字符串
     */
    private String convertToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
    
    /**
     * JSON字符串转Map
     */
    private Map<String, Object> convertFromJson(String json) {
        if (json == null || json.isEmpty()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            return new HashMap<>();
        }
    }
    
    /**
     * 转换为Money对象
     */
    private Money convertToMoney(Object value) {
        if (value == null) {
            return Money.zeroCny();
        }
        if (value instanceof Number) {
            return Money.cny(((Number) value).doubleValue());
        }
        return Money.zeroCny();
    }
}
