package com.atun.brain.home.infrastructure.converter;

import com.atun.brain.home.application.service.dto.TransactionResponse;
import com.atun.brain.home.domain.model.entity.Transaction;
import com.atun.brain.home.domain.model.vo.TransactionType;
import com.atun.brain.home.infrastructure.persistence.po.TransactionPO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 交易转换器
 * 负责 DTO、Entity、PO 之间的转换
 *
 * @author atun-brain
 * @since 1.0
 */
@Component
public class TransactionConverter {

    /**
     * 将领域实体转换为响应 DTO
     *
     * @param transaction 交易实体
     * @param categoryName 分类名称
     * @return 交易响应
     */
    public TransactionResponse toResponse(Transaction transaction, String categoryName) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .type(transaction.getType() != null ? transaction.getType().name() : null)
                .amount(transaction.getAmount())
                .categoryId(transaction.getCategoryId())
                .categoryName(categoryName)
                .description(transaction.getDescription())
                .transactionTime(transaction.getTransactionTime())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

    /**
     * 将领域实体转换为 PO
     *
     * @param transaction 交易实体
     * @return 交易 PO
     */
    public TransactionPO toPO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return TransactionPO.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .type(transaction.getType() != null ? transaction.getType().name() : null)
                .amount(transaction.getAmount())
                .categoryId(transaction.getCategoryId())
                .description(transaction.getDescription())
                .transactionTime(transaction.getTransactionTime())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .deleted(transaction.getDeleted() != null && transaction.getDeleted() ? 1 : 0)
                .build();
    }

    /**
     * 将 PO 转换为领域实体
     *
     * @param po 交易 PO
     * @return 交易实体
     */
    public Transaction toEntity(TransactionPO po) {
        if (po == null) {
            return null;
        }
        return Transaction.builder()
                .id(po.getId())
                .userId(po.getUserId())
                .type(parseTransactionType(po.getType()))
                .amount(po.getAmount())
                .categoryId(po.getCategoryId())
                .description(po.getDescription())
                .transactionTime(po.getTransactionTime())
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .deleted(po.getDeleted() != null && po.getDeleted() == 1)
                .build();
    }

    /**
     * 解析交易类型
     *
     * @param type 类型字符串
     * @return 交易类型枚举
     */
    private TransactionType parseTransactionType(String type) {
        if (type == null) {
            return null;
        }
        try {
            return TransactionType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("无效的交易类型：" + type);
        }
    }
}
