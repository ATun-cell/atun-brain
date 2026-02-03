package com.atun.brain.domain.finance.aggregate;

import com.atun.brain.domain.finance.entity.Category;
import com.atun.brain.domain.finance.valueobject.Money;
import com.atun.brain.domain.finance.valueobject.TransactionType;
import com.atun.brain.domain.shared.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 交易聚合根 
 * 包含交易的业务逻辑：分类、标签、验证等
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Transaction extends BaseEntity {
    
    /** 用户ID */
    private Long userId;
    
    /** 关联的消息ID（来源对话） */
    private Long messageId;
    
    /** 交易金额 */
    private Money amount;
    
    /** 交易类型（收入/支出） */
    private TransactionType type;
    
    /** 分类 */
    private Category category;
    
    /** 交易日期 */
    private LocalDate transactionDate;
    
    /** 描述/备注 */
    private String description;
    
    /** 标签列表 */
    private List<String> tags;
    
    /**
     * 创建一个新交易
     */
    public static Transaction create(Long userId, Money amount, TransactionType type,
                                     Category category, LocalDate transactionDate,
                                     String description) {
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setCategory(category);
        transaction.setTransactionDate(transactionDate);
        transaction.setDescription(description);
        transaction.setTags(new ArrayList<>());
        
        transaction.validate();
        return transaction;
    }
    
    /**
     * 添加标签
     */
    public void addTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            throw new IllegalArgumentException("标签不能为空");
        }
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }
    
    /**
     * 移除标签
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }
    
    /**
     * 更改分类
     */
    public void changeCategory(Category newCategory) {
        if (newCategory == null) {
            throw new IllegalArgumentException("分类不能为空");
        }
        // 验证分类类型与交易类型是否匹配
        if (!newCategory.getType().equals(this.type.getCode())) {
            throw new IllegalArgumentException(
                String.format("分类类型(%s)与交易类型(%s)不匹配", 
                    newCategory.getType(), this.type.getCode())
            );
        }
        this.category = newCategory;
    }
    
    /**
     * 修改交易金额
     */
    public void changeAmount(Money newAmount) {
        if (newAmount == null) {
            throw new IllegalArgumentException("金额不能为空");
        }
        if (!newAmount.isPositive()) {
            throw new IllegalArgumentException("金额必须为正数");
        }
        this.amount = newAmount;
    }
    
    /**
     * 修改交易日期
     */
    public void changeTransactionDate(LocalDate newDate) {
        if (newDate == null) {
            throw new IllegalArgumentException("交易日期不能为空");
        }
        if (newDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("交易日期不能是未来日期");
        }
        this.transactionDate = newDate;
    }
    
    /**
     * 验证交易数据
     */
    private void validate() {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (amount == null) {
            throw new IllegalArgumentException("金额不能为空");
        }
        if (!amount.isPositive()) {
            throw new IllegalArgumentException("金额必须为正数");
        }
        if (type == null) {
            throw new IllegalArgumentException("交易类型不能为空");
        }
        if (category == null) {
            throw new IllegalArgumentException("分类不能为空");
        }
        if (transactionDate == null) {
            throw new IllegalArgumentException("交易日期不能为空");
        }
        if (transactionDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("交易日期不能是未来日期");
        }
        // 验证分类类型与交易类型是否匹配
        if (!category.getType().equals(type.getCode())) {
            throw new IllegalArgumentException(
                String.format("分类类型(%s)与交易类型(%s)不匹配", 
                    category.getType(), type.getCode())
            );
        }
    }
    
    /**
     * 判断是否为支出
     */
    public boolean isExpense() {
        return type.isExpense();
    }
    
    /**
     * 判断是否为收入
     */
    public boolean isIncome() {
        return type.isIncome();
    }
}
