package com.atun.brain.infrastructure.persistence.mybatis.po;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 生成报告PO
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
public class GenerateReportPO {
    
    private Long id;
    private Long userId;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String reportType;
    private BigDecimal totalExpense;
    private BigDecimal totalIncome;
    private BigDecimal netAmount;
    private String categoryStatistics;
    private String analysisText;
    private String vectorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
