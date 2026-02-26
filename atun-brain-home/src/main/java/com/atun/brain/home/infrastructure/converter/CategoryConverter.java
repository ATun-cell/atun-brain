package com.atun.brain.home.infrastructure.converter;

import com.atun.brain.home.domain.model.entity.Category;
import com.atun.brain.home.domain.model.vo.TransactionType;
import com.atun.brain.home.infrastructure.persistence.po.CategoryPO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 分类转换器
 * 负责 Entity 和 PO 之间的转换
 *
 * @author atun-brain
 * @since 1.0
 */
@Component
public class CategoryConverter {

    /**
     * 将领域实体转换为 PO
     *
     * @param category 分类实体
     * @return 分类 PO
     */
    public CategoryPO toPO(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryPO.builder()
                .id(category.getId())
                .userId(category.getUserId())
                .type(category.getType() != null ? category.getType().name() : null)
                .name(category.getName())
                .icon(category.getIcon())
                .sortOrder(category.getSortOrder())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt() != null ? category.getUpdatedAt() : LocalDateTime.now())
                .deleted(category.getDeleted() != null && category.getDeleted() ? 1 : 0)
                .build();
    }

    /**
     * 将 PO 转换为领域实体
     *
     * @param po 分类 PO
     * @return 分类实体
     */
    public Category toEntity(CategoryPO po) {
        if (po == null) {
            return null;
        }
        return Category.builder()
                .id(po.getId())
                .userId(po.getUserId())
                .type(parseTransactionType(po.getType()))
                .name(po.getName())
                .icon(po.getIcon())
                .sortOrder(po.getSortOrder())
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
            return null;
        }
    }
}
