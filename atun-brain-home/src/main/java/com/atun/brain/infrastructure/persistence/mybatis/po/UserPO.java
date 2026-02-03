package com.atun.brain.infrastructure.persistence.mybatis.po;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户PO
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
public class UserPO {
    
    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private String preferences;
    private Boolean active;
    private LocalDateTime lastLoginAt;
    private Boolean emailVerified;
    private String role;
    private String avatarUrl;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
