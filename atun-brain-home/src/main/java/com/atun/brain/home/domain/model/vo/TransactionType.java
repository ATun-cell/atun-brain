package com.atun.brain.home.domain.model.vo;

/**
 * 交易类型枚举值对象
 *
 * @author atun-brain
 * @since 1.0
 */
public enum TransactionType {

    /**
     * 收入
     */
    INCOME("收入"),

    /**
     * 支出
     */
    EXPENSE("支出");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    public String getDescription() {
        return description;
    }
}
