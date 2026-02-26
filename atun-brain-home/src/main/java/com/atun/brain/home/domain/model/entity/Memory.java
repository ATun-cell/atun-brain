package com.atun.brain.home.domain.model.entity;

import com.atun.brain.home.domain.model.vo.MemoryType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 记忆实体 - 聚合根
 * 记录用户的个人记忆（对话、事件、知识等）
 *
 * @author atun-brain
 * @since 1.0
 */
@Data
@Builder
public class Memory {

    /**
     * 记忆 ID
     */
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 记忆类型：CONVERSATION(对话) / EVENT(事件) / KNOWLEDGE(知识) /
     * PERSON(人物) / PLACE(地点) / MEDIA(媒体)
     */
    private MemoryType type;

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
     * 关联的会话 ID
     */
    private String sessionId;

    /**
     * 元数据（JSON 格式：时间、地点、人物、情绪等）
     */
    private String metadata;

    /**
     * 向量 ID（用于 RAG 检索）
     */
    private String vectorId;

    /**
     * 是否重要：0-否，1-是
     */
    private Boolean isImportant;

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
