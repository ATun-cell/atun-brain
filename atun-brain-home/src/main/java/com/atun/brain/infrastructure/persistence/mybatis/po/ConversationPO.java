package com.atun.brain.infrastructure.persistence.mybatis.po;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 会话PO
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
public class ConversationPO {
    
    private Long id;
    private Long userId;
    private String sessionId;
    private String agentType;
    private LocalDateTime lastActiveAt;
    private Boolean archived;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
