package com.atun.brain.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 生成报告
 *
 * @author lij
 * @date 2026/02/02
 */
@Data
public class GeneratedReport {
    
    /** 身份证 */
    private Long id;
    
    /** 用户ID */
    private Long userId;
    
    /** 句号开始 */
    private LocalDate periodStart;
    
    /** 句号结束 */
    private LocalDate periodEnd;
    
    /** 报告数据 */
    private String reportData; // JSON字符串
    
    /** 矢量标识 */
    private String vectorId;
    
    /** 创建于 */
    private LocalDateTime createdAt;
}
