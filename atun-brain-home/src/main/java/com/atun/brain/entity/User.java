package com.atun.brain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    
    /** 身份证 */
    private Long id;
    
    /** 用户名 */
    private String username;
    
    /** 电子邮件 */
    private String email;
    
    /** 密码哈希 */
    private String passwordHash;
    
    /** 偏好 */
    private String preferences; // JSON字符串
    
    /** 创建于 */
    private LocalDateTime createdAt;
    
    /** 更新于 */
    private LocalDateTime updatedAt;
}
