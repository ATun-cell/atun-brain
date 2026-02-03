package com.atun.brain.domain.finance.aggregate;

import com.atun.brain.domain.finance.entity.Category;
import com.atun.brain.domain.finance.valueobject.Money;
import com.atun.brain.domain.finance.valueobject.Period;
import com.atun.brain.domain.shared.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 预算聚合根 
 * 包含周期计算、超支检测逻辑
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Budget extends BaseEntity {
    
    /** 用户ID */
    private Long userId;
    
    /** 关联的分类 */
    private Category category;
    
    /** 预算金额 */
    private Money amount;
    
    /** 预算周期（月度/周） */
    private String periodType; // "MONTHLY" 或 "WEEKLY"
    
    /** 年份 */
    private Integer year;
    
    /** 月份（仅月度预算使用） */
    private Integer month;
    
    /**
     * 创建月度预算
     */
    public static Budget createMonthly(Long userId, Category category, 
                                       Money amount, int year, int month) {
        Budget budget = new Budget();
        budget.setUserId(userId);
        budget.setCategory(category);
        budget.setAmount(amount);
        budget.setPeriodType("MONTHLY");
        budget.setYear(year);
        budget.setMonth(month);
        
        budget.validate();
        return budget;
    }
    
    /**
     * 创建周度预算
     */
    public static Budget createWeekly(Long userId, Category category, Money amount) {
        Budget budget = new Budget();
        budget.setUserId(userId);
        budget.setCategory(category);
        budget.setAmount(amount);
        budget.setPeriodType("WEEKLY");
        
        budget.validate();
        return budget;
    }
    
    /**
     * 检查是否超预算
     * @param spent 已花费金额
     * @return true 表示超预算
     */
    public boolean isExceeded(Money spent) {
        return spent.greaterThan(this.amount);
    }
    
    /**
     * 计算剩余预算
     * @param spent 已花费金额
     * @return 剩余金额
     */
    public Money getRemaining(Money spent) {
        return this.amount.subtract(spent);
    }
    
    /**
     * 计算使用率
     * @param spent 已花费金额
     * @return 使用率（0-100）
     */
    public double getUsagePercentage(Money spent) {
        if (this.amount.isZero()) {
            return 0.0;
        }
        return spent.getAmount()
                   .divide(this.amount.getAmount(), 4, java.math.RoundingMode.HALF_UP)
                   .multiply(java.math.BigDecimal.valueOf(100))
                   .doubleValue();
    }
    
    /**
     * 获取预算周期
     */
    public Period getPeriod() {
        if ("MONTHLY".equals(periodType)) {
            return Period.ofMonth(year, month);
        } else if ("WEEKLY".equals(periodType)) {
            return Period.thisWeek();
        }
        throw new IllegalStateException("未知的预算周期类型: " + periodType);
    }
    
    /**
     * 调整预算金额
     */
    public void adjustAmount(Money newAmount) {
        if (newAmount == null || !newAmount.isPositive()) {
            throw new IllegalArgumentException("预算金额必须为正数");
        }
        this.amount = newAmount;
    }
    
    /**
     * 验证预算数据
     */
    private void validate() {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (category == null) {
            throw new IllegalArgumentException("分类不能为空");
        }
        if (amount == null || !amount.isPositive()) {
            throw new IllegalArgumentException("预算金额必须为正数");
        }
        if (periodType == null || (!"MONTHLY".equals(periodType) && !"WEEKLY".equals(periodType))) {
            throw new IllegalArgumentException("预算周期类型必须为MONTHLY或WEEKLY");
        }
        if ("MONTHLY".equals(periodType)) {
            if (year == null || month == null) {
                throw new IllegalArgumentException("月度预算必须指定年份和月份");
            }
            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("月份必须在1-12之间");
            }
        }
    }
}
