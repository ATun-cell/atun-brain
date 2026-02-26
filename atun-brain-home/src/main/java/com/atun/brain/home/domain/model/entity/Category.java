package com.atun.brain.home.domain.model.entity;

import com.atun.brain.home.domain.model.vo.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 分类实体 - 聚合根
 * 交易分类管理（收入分类/支出分类）
 *
 * @author atun-brain
 * @since 1.0
 */
@Data
@Builder
public class Category {

    /**
     * 分类 ID
     */
    private Long id;

    /**
     * 用户 ID（0 表示系统预置分类）
     */
    private Long userId;

    /**
     * 分类类型：INCOME(收入) / EXPENSE(支出)
     */
    private TransactionType type;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类图标
     */
    private String icon;

    /**
     * 排序值
     */
    private Integer sortOrder;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 是否已删除
     */
    private Boolean deleted;
}
