package com.atun.brain.infrastructure.persistence.mybatis.po;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 消息 PO
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
public class MessagePO {
    
    private Long id;
    private Long conversationId;
    private String role;
    private String content;
    private String metadata;
    private String vectorId;
    private LocalDateTime timestamp;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
