package com.atun.brain.home.infrastructure.persistence.po;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 分类持久化对象
 * 对应数据库表：t_finance_category
 *
 * @author atun-brain
 * @since 1.0
 */
@Data
@Builder
public class CategoryPO {

    /**
     * 分类 ID
     */
    private Long id;

    /**
     * 用户 ID（0 表示系统预置分类）
     */
    private Long userId;

    /**
     * 分类类型：INCOME/EXPENSE
     */
    private String type;

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
     * 是否已删除：0-否，1-是
     */
    private Integer deleted;
}
