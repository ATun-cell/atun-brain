package com.atun.brain.domain.finance.valueobject;

import lombok.Getter;

/**
 * 交易类型枚举 - 收入/支出
 *
 * @author lij
 * @date 2026/02/03
 */
@Getter
public enum TransactionType {
    
    /** 支出 */
    EXPENSE("EXPENSE", "支出"),
    
    /** 收入 */
    INCOME("INCOME", "收入");
    
    private final String code;
    private final String description;
    
    TransactionType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 从代码获取枚举
     */
    public static TransactionType fromCode(String code) {
        for (TransactionType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的交易类型: " + code);
    }
    
    /**
     * 是否为支出
     */
    public boolean isExpense() {
        return this == EXPENSE;
    }
    
    /**
     * 是否为收入
     */
    public boolean isIncome() {
        return this == INCOME;
    }
}
