package com.atun.brain.domain.user.entity;

import com.atun.brain.domain.shared.BaseEntity;
import com.atun.brain.domain.user.valueobject.Email;
import com.atun.brain.domain.user.valueobject.UserPreferences;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户实体
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    
    /** 用户名 */
    private String username;
    
    /** 邮箱 */
    private Email email;
    
    /** 密码哈希（存储加密后的密码） */
    private String passwordHash;
    
    /** 用户偏好设置 */
    private UserPreferences preferences;
    
    /** 是否激活 */
    private boolean active;
    
    /** 最后登录时间 */
    private LocalDateTime lastLoginAt;
    
    /** 是否验证邮箱 */
    private boolean emailVerified;
    
    /** 用户角色（ADMIN/USER） */
    private String role;
    
    /** 头像 URL */
    private String avatarUrl;
    
    /** 简介 */
    private String bio;
    
    /**
     * 构造方法
     */
    public User() {
        super();
        this.active = true;
        this.emailVerified = false;
        this.role = "USER";
        this.preferences = UserPreferences.createDefault();
    }
    
    /**
     * 创建新用户
     */
    public static User create(String username, Email email, String passwordHash) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (email == null) {
            throw new IllegalArgumentException("邮箱不能为空");
        }
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        User user = new User();
        user.setUsername(username.trim());
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        return user;
    }
    
    /**
     * 更新用户名
     */
    public void updateUsername(String newUsername) {
        if (newUsername == null || newUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (newUsername.length() < 3 || newUsername.length() > 50) {
            throw new IllegalArgumentException("用户名长度必须在 3-50 之间");
        }
        this.username = newUsername.trim();
    }
    
    /**
     * 更新邮箱
     */
    public void updateEmail(Email newEmail) {
        if (newEmail == null) {
            throw new IllegalArgumentException("邮箱不能为空");
        }
        this.email = newEmail;
        this.emailVerified = false; // 需要重新验证
    }
    
    /**
     * 更新密码
     */
    public void updatePassword(String newPasswordHash) {
        if (newPasswordHash == null || newPasswordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        this.passwordHash = newPasswordHash;
    }
    
    /**
     * 验证邮箱
     */
    public void verifyEmail() {
        this.emailVerified = true;
    }
    
    /**
     * 更新偏好设置
     */
    public void updatePreferences(UserPreferences newPreferences) {
        if (newPreferences == null) {
            throw new IllegalArgumentException("用户偏好设置不能为空");
        }
        this.preferences = newPreferences;
    }
    
    /**
     * 记录登录
     */
    public void recordLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }
    
    /**
     * 禁用用户
     */
    public void deactivate() {
        this.active = false;
    }
    
    /**
     * 启用用户
     */
    public void activate() {
        this.active = true;
    }
    
    /**
     * 更新头像
     */
    public void updateAvatar(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    /**
     * 更新简介
     */
    public void updateBio(String bio) {
        if (bio != null && bio.length() > 500) {
            throw new IllegalArgumentException("简介长度不能超过 500 字符");
        }
        this.bio = bio;
    }
    
    /**
     * 判断是否为管理员
     */
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
    
    /**
     * 设置为管理员
     */
    public void setAsAdmin() {
        this.role = "ADMIN";
    }
    
    /**
     * 设置为普通用户
     */
    public void setAsUser() {
        this.role = "USER";
    }
}
