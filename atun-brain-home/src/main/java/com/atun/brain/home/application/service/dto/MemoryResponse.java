package com.atun.brain.home.application.service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 记忆响应 DTO
 *
 * @author atun-brain
 * @since 1.0
 */
@Data
@Builder
public class MemoryResponse {

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
     * 元数据
     */
    private String metadata;

    /**
     * 是否重要
     */
    private Boolean isImportant;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
