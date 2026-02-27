package com.atun.brain.home.infrastructure.persistence.po;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会话记忆持久化对象
 * 对应数据库表：t_chat_memory
 *
 * @author atun-brain
 * @since 1.0
 */
@Data
@Builder
public class ChatMemoryPO {

    /**
     * 主键 ID
     */
    private Long id;

    /**
     * 记忆标识（userId:sessionId）
     */
    private String memoryId;

    /**
     * 消息列表 JSON
     */
    private String messagesJson;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
