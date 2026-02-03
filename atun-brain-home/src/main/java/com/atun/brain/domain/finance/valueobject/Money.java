package com.atun.brain.domain.finance.valueobject;

import lombok.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * 金额值对象 - 不可变的金额表示
 * 包含金额和货币类型，提供金额计算和校验
 *
 * @author lij
 * @date 2026/02/03
 */
@Value
public class Money {
    
    BigDecimal amount;
    Currency currency;
    
    /**
     * 创建金额对象
     */
    public Money(BigDecimal amount, Currency currency) {
        if (amount == null) {
            throw new IllegalArgumentException("金额不能为空");
        }
        if (currency == null) {
            throw new IllegalArgumentException("货币类型不能为空");
        }
        // 保留2位小数
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
    }
    
    /**
     * 创建人民币金额
     */
    public static Money cny(BigDecimal amount) {
        return new Money(amount, Currency.getInstance("CNY"));
    }
    
    /**
     * 创建人民币金额（从double）
     */
    public static Money cny(double amount) {
        return cny(BigDecimal.valueOf(amount));
    }
    
    /**
     * 创建零金额
     */
    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }
    
    /**
     * 创建人民币零金额
     */
    public static Money zeroCny() {
        return zero(Currency.getInstance("CNY"));
    }
    
    /**
     * 加法
     */
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("不能对不同货币进行运算");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }
    
    /**
     * 减法
     */
    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("不能对不同货币进行运算");
        }
        return new Money(this.amount.subtract(other.amount), this.currency);
    }
    
    /**
     * 乘法
     */
    public Money multiply(BigDecimal multiplier) {
        return new Money(this.amount.multiply(multiplier), this.currency);
    }
    
    /**
     * 除法
     */
    public Money divide(BigDecimal divisor) {
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("除数不能为零");
        }
        return new Money(this.amount.divide(divisor, 2, RoundingMode.HALF_UP), this.currency);
    }
    
    /**
     * 是否为正数
     */
    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * 是否为负数
     */
    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }
    
    /**
     * 是否为零
     */
    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * 比较大小
     */
    public int compareTo(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("不能比较不同货币");
        }
        return this.amount.compareTo(other.amount);
    }
    
    /**
     * 是否大于
     */
    public boolean greaterThan(Money other) {
        return compareTo(other) > 0;
    }
    
    /**
     * 是否小于
     */
    public boolean lessThan(Money other) {
        return compareTo(other) < 0;
    }
    
    @Override
    public String toString() {
        return currency.getSymbol() + amount;
    }
}
