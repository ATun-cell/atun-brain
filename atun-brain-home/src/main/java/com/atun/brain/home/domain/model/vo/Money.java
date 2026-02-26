package com.atun.brain.home.domain.model.vo;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * 金额值对象
 * 封装金额的加减运算和比较
 *
 * @author atun-brain
 * @since 1.0
 */
@Getter
public class Money {

    /**
     * 金额数值
     */
    private final BigDecimal amount;

    /**
     * 构造函数
     *
     * @param amount 金额
     */
    public Money(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("金额必须非负");
        }
        this.amount = amount;
    }

    /**
     * 加法
     *
     * @param other 其他金额
     * @return 新的金额对象
     */
    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    /**
     * 减法
     *
     * @param other 其他金额
     * @return 新的金额对象
     */
    public Money subtract(Money other) {
        return new Money(this.amount.subtract(other.amount));
    }

    /**
     * 是否大于指定金额
     *
     * @param other 其他金额
     * @return true/false
     */
    public boolean isGreaterThan(Money other) {
        return this.amount.compareTo(other.amount) > 0;
    }

    /**
     * 转为 BigDecimal
     *
     * @return 金额数值
     */
    public BigDecimal toBigDecimal() {
        return amount;
    }
}
