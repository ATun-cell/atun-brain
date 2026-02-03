package com.atun.brain.entity;


import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息
 *
 * @author lij
 * @date 2026/02/02
 */
@Data
public class Message {
    
    /** 身份证 */
    private Long id;
    
    /** 对话ID */
    private Long conversationId;
    
    /** 角色 */
    private String role; // user/assistant/system
    
    /** 内容 */
    private String content;
    
    /** 元数据 */
    private String metadata; // JSON字符串
    
    /** 矢量标识 */
    private String vectorId;
    
    /** 创建于 */
    private LocalDateTime createdAt;
}
