package com.atun.brain.domain.finance.valueobject;

import lombok.Value;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 周期值对象 - 表示一个时间范围
 *
 * @author lij
 * @date 2026/02/03
 */
@Value
public class Period {
    
    LocalDate startDate;
    LocalDate endDate;
    
    public Period(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("开始日期和结束日期不能为空");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    /**
     * 创建本周周期（周一到周日）
     */
    public static Period thisWeek() {
        LocalDate now = LocalDate.now();
        LocalDate monday = now.minusDays(now.getDayOfWeek().getValue() - 1);
        LocalDate sunday = monday.plusDays(6);
        return new Period(monday, sunday);
    }
    
    /**
     * 创建上周周期
     */
    public static Period lastWeek() {
        Period thisWeek = thisWeek();
        return new Period(
            thisWeek.startDate.minusWeeks(1),
            thisWeek.endDate.minusWeeks(1)
        );
    }
    
    /**
     * 创建本月周期
     */
    public static Period thisMonth() {
        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.withDayOfMonth(1);
        LocalDate lastDay = now.withDayOfMonth(now.lengthOfMonth());
        return new Period(firstDay, lastDay);
    }
    
    /**
     * 创建上月周期
     */
    public static Period lastMonth() {
        LocalDate now = LocalDate.now().minusMonths(1);
        LocalDate firstDay = now.withDayOfMonth(1);
        LocalDate lastDay = now.withDayOfMonth(now.lengthOfMonth());
        return new Period(firstDay, lastDay);
    }
    
    /**
     * 创建本年周期
     */
    public static Period thisYear() {
        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.withDayOfYear(1);
        LocalDate lastDay = now.withDayOfYear(now.lengthOfYear());
        return new Period(firstDay, lastDay);
    }
    
    /**
     * 创建指定月份的周期
     */
    public static Period ofMonth(int year, int month) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
        return new Period(firstDay, lastDay);
    }
    
    /**
     * 判断日期是否在周期内
     */
    public boolean contains(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
    
    /**
     * 获取周期天数
     */
    public long getDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
    
    /**
     * 判断是否与另一个周期重叠
     */
    public boolean overlaps(Period other) {
        return !this.endDate.isBefore(other.startDate) 
            && !other.endDate.isBefore(this.startDate);
    }
}
