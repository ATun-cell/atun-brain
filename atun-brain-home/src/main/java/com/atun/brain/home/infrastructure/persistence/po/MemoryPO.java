package com.atun.brain.home.infrastructure.persistence.po;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 记忆持久化对象
 * 对应数据库表：t_memory
 *
 * @author atun-brain
 * @since 1.0
 */
@Data
@Builder
public class MemoryPO {

    /**
     * 记忆 ID
     */
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 记忆类型
     */
    private String type;

    /**
     * 记忆标题
     */
    private String title;

    /**
     * 记忆内容
     */
    private String content;

    /**
     * 记忆摘要
     */
    private String summary;

    /**
     * 会话 ID
     */
    private String sessionId;

    /**
     * 元数据（JSON）
     */
    private String metadata;

    /**
     * 向量 ID
     */
    private String vectorId;

    /**
     * 是否重要
     */
    private Integer isImportant;

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
