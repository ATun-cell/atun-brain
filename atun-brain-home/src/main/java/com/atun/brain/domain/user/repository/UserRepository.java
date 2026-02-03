package com.atun.brain.domain.user.repository;

import com.atun.brain.domain.user.entity.User;
import com.atun.brain.domain.user.valueobject.Email;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户仓储接口
 *
 * @author lij
 * @date 2026/02/03
 */
public interface UserRepository {
    
    /**
     * 保存用户
     */
    User save(User user);
    
    /**
     * 根据ID查找用户
     */
    Optional<User> findById(Long id);
    
    /**
     * 根据用户名查找
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据邮箱查找
     */
    Optional<User> findByEmail(Email email);
    
    /**
     * 查找所有激活用户
     */
    List<User> findAllActive();
    
    /**
     * 查找所有用户（包括禁用）
     */
    List<User> findAll();
    
    /**
     * 查找指定时间后注册的用户
     */
    List<User> findByCreatedAfter(LocalDateTime dateTime);
    
    /**
     * 查找指定时间后登录过的用户
     */
    List<User> findByLastLoginAfter(LocalDateTime dateTime);
    
    /**
     * 查找已验证邮箱的用户
     */
    List<User> findByEmailVerified(boolean verified);
    
    /**
     * 查找管理员用户
     */
    List<User> findAdmins();
    
    /**
     * 更新用户
     */
    void update(User user);
    
    /**
     * 删除用户
     */
    void deleteById(Long id);
    
    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(Email email);
    
    /**
     * 统计用户总数
     */
    long count();
    
    /**
     * 统计激活用户数
     */
    long countActive();
}
