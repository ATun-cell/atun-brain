package com.atun.brain.infrastructure.persistence.mybatis.converter;

import com.atun.brain.domain.user.entity.User;
import com.atun.brain.domain.user.valueobject.Email;
import com.atun.brain.domain.user.valueobject.UserPreferences;
import com.atun.brain.infrastructure.persistence.mybatis.po.UserPO;
import org.springframework.stereotype.Component;

/**
 * 用户转换器
 * 负责User领域对象与UserPO之间的转换
 *
 * @author lij
 * @date 2026/02/03
 */
@Component
public class UserConverter {
    
    /**
     * 将领域对象转换为PO
     */
    public UserPO toPO(User user) {
        if (user == null) {
            return null;
        }
        
        UserPO po = new UserPO();
        po.setId(user.getId());
        po.setUsername(user.getUsername());
        po.setEmail(user.getEmail() != null ? user.getEmail().getValue() : null);
        po.setPasswordHash(user.getPasswordHash());
        po.setPreferences(user.getPreferences() != null ? user.getPreferences().toJson() : null);
        po.setActive(user.isActive());
        po.setLastLoginAt(user.getLastLoginAt());
        po.setEmailVerified(user.isEmailVerified());
        po.setRole(user.getRole());
        po.setAvatarUrl(user.getAvatarUrl());
        po.setBio(user.getBio());
        po.setCreatedAt(user.getCreatedAt());
        po.setUpdatedAt(user.getUpdatedAt());
        
        return po;
    }
    
    /**
     * 将PO转换为领域对象
     */
    public User toDomain(UserPO po) {
        if (po == null) {
            return null;
        }
        
        User user = new User();
        user.setId(po.getId());
        user.setUsername(po.getUsername());
        user.setEmail(po.getEmail() != null ? Email.of(po.getEmail()) : null);
        user.setPasswordHash(po.getPasswordHash());
        user.setPreferences(po.getPreferences() != null ? 
                UserPreferences.fromJson(po.getPreferences()) : UserPreferences.createDefault());
        user.setActive(po.getActive() != null ? po.getActive() : true);
        user.setLastLoginAt(po.getLastLoginAt());
        user.setEmailVerified(po.getEmailVerified() != null ? po.getEmailVerified() : false);
        user.setRole(po.getRole() != null ? po.getRole() : "USER");
        user.setAvatarUrl(po.getAvatarUrl());
        user.setBio(po.getBio());
        user.setCreatedAt(po.getCreatedAt());
        user.setUpdatedAt(po.getUpdatedAt());
        
        return user;
    }
}
