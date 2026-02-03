package com.atun.brain.domain.user.valueobject;

import lombok.Value;

import java.util.regex.Pattern;

/**
 * 邮箱值对象
 * 表示用户的邮箱地址
 *
 * @author lij
 * @date 2026/02/03
 */
@Value
public class Email {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    String value;
    
    private Email(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱地址不能为空");
        }
        if (!isValid(value)) {
            throw new IllegalArgumentException("无效的邮箱地址: " + value);
        }
        this.value = value.toLowerCase().trim();
    }
    
    /**
     * 创建邮箱对象
     */
    public static Email of(String value) {
        return new Email(value);
    }
    
    /**
     * 验证邮箱格式
     */
    public static boolean isValid(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * 获取域名
     */
    public String getDomain() {
        int atIndex = value.indexOf('@');
        if (atIndex > 0) {
            return value.substring(atIndex + 1);
        }
        return "";
    }
    
    /**
     * 获取用户名
     */
    public String getLocalPart() {
        int atIndex = value.indexOf('@');
        if (atIndex > 0) {
            return value.substring(0, atIndex);
        }
        return value;
    }
    
    /**
     * 判断是否为企业邮箱
     */
    public boolean isCorporateEmail() {
        String domain = getDomain().toLowerCase();
        return !domain.equals("gmail.com") && 
               !domain.equals("yahoo.com") && 
               !domain.equals("outlook.com") &&
               !domain.equals("hotmail.com") &&
               !domain.equals("qq.com") &&
               !domain.equals("163.com") &&
               !domain.equals("126.com");
    }
    
    @Override
    public String toString() {
        return value;
    }
}
