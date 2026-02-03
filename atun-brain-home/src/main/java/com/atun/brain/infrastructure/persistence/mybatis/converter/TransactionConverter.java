package com.atun.brain.infrastructure.persistence.mybatis.converter;

import com.atun.brain.domain.finance.aggregate.Transaction;
import com.atun.brain.domain.finance.entity.Category;
import com.atun.brain.domain.finance.valueobject.Money;
import com.atun.brain.domain.finance.valueobject.TransactionType;
import com.atun.brain.infrastructure.persistence.mybatis.po.TransactionPO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 交易聚合根与PO之间的转换器
 *
 * @author lij
 * @date 2026/02/03
 */
@Component
public class TransactionConverter {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Currency DEFAULT_CURRENCY = Currency.getInstance("CNY");
    
    /**
     * 领域模型 -> PO（用于持久化）
     */
    public TransactionPO toPO(Transaction domain) {
        if (domain == null) {
            return null;
        }
        
        TransactionPO po = new TransactionPO();
        po.setId(domain.getId());
        po.setUserId(domain.getUserId());
        po.setMessageId(domain.getMessageId());
        po.setAmount(domain.getAmount().getAmount());
        po.setType(domain.getType().getCode());
        po.setCategoryId(domain.getCategory() != null ? domain.getCategory().getId() : null);
        po.setTransactionDate(domain.getTransactionDate());
        po.setDescription(domain.getDescription());
        po.setTags(convertTagsToJson(domain.getTags()));
        po.setCreatedAt(domain.getCreatedAt());
        po.setUpdatedAt(domain.getUpdatedAt());
        
        return po;
    }
    
    /**
     * PO -> 领域模型（从数据库加载）
     */
    public Transaction toDomain(TransactionPO po) {
        if (po == null) {
            return null;
        }
        
        Transaction domain = new Transaction();
        domain.setId(po.getId());
        domain.setUserId(po.getUserId());
        domain.setMessageId(po.getMessageId());
        domain.setAmount(Money.cny(po.getAmount()));
        domain.setType(TransactionType.fromCode(po.getType()));
        domain.setTransactionDate(po.getTransactionDate());
        domain.setDescription(po.getDescription());
        domain.setTags(convertTagsFromJson(po.getTags()));
        domain.setCreatedAt(po.getCreatedAt());
        domain.setUpdatedAt(po.getUpdatedAt());
        
        return domain;
    }
    
    /**
     * PO列表 -> 领域模型列表
     */
    public List<Transaction> toDomainList(List<TransactionPO> pos) {
        if (pos == null) {
            return new ArrayList<>();
        }
        return pos.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * 标签列表转JSON字符串
     */
    private String convertTagsToJson(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(tags);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
    
    /**
     * JSON字符串转标签列表
     */
    private List<String> convertTagsFromJson(String json) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }
}
