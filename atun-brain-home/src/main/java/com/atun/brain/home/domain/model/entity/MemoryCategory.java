package com.atun.brain.home.domain.model.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 记忆分类实体 - 聚合根
 * 用于记忆的标签和分类管理
 *
 * @author atun-brain
 * @since 1.0
 */
@Data
@Builder
public class MemoryCategory {

    /**
     * 分类 ID
     */
    private Long id;

    /**
     * 用户 ID（0 表示系统预置分类）
     */
    private Long userId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类 ID
     */
    private Long parentId;

    /**
     * 分类层级
     */
    private Integer level;

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
