package com.atun.brain.home.infrastructure.persistence.po;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 记忆分类持久化对象
 * 对应数据库表：t_memory_category
 *
 * @author atun-brain
 * @since 1.0
 */
@Data
@Builder
public class MemoryCategoryPO {

    /**
     * 分类 ID
     */
    private Long id;

    /**
     * 用户 ID
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
    private Integer deleted;
}
