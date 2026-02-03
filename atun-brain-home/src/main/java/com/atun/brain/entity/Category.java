package com.atun.brain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 类别
 *
 * @author lij
 * @date 2026/02/02
 */
@Data
public class Category {
    
    /** 身份证 */
    private Long id;
    
    /** 名称 */
    private String name;
    
    /** 类型 */
    private String type; // 支出/收入
    
    /** 父ID */
    private Long parentId;
    
    /** 创建于 */
    private LocalDateTime createdAt;
}
