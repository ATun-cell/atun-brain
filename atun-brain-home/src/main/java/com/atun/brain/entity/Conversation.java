package com.atun.brain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 对话
 *
 * @author lij
 * @date 2026/02/02
 */
@Data
public class Conversation {
    
    /** 身份证 */
    private Long id;
    
    /** 用户ID */
    private Long userId;
    
    /** 会话编号 */
    private String sessionId;
    
    /** 代理类型 */
    private String agentType;
    
    /** 创建于 */
    private LocalDateTime createdAt;
    
    /** 最后活跃于 */
    private LocalDateTime lastActiveAt;
}
