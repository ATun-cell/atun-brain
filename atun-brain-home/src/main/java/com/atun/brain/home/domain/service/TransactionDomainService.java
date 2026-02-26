package com.atun.brain.home.domain.service;

import com.atun.brain.home.domain.model.entity.Transaction;
import com.atun.brain.home.domain.model.vo.Money;
import com.atun.brain.home.domain.model.vo.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 交易领域服务
 * 处理跨聚合的业务逻辑
 *
 * @author atun-brain
 * @since 1.0
 */
@Slf4j
@Component
public class TransactionDomainService {

    /**
     * 验证交易金额是否有效
     *
     * @param transaction 交易
     * @return true/false
     */
    public boolean validateAmount(Transaction transaction) {
        if (transaction == null) {
            return false;
        }

        // 金额不能为空
        if (transaction.getAmount() == null) {
            log.warn("交易金额不能为空");
            return false;
        }

        // 金额必须大于 0
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("交易金额必须大于 0");
            return false;
        }

        // 金额不能超过最大值（根据业务需求设定）
        if (transaction.getAmount().compareTo(new BigDecimal("1000000000")) > 0) {
            log.warn("交易金额超过限制");
            return false;
        }

        return true;
    }

    /**
     * 验证交易类型
     *
     * @param transaction 交易
     * @return true/false
     */
    public boolean validateTransactionType(Transaction transaction) {
        if (transaction == null || transaction.getType() == null) {
            return false;
        }

        TransactionType type = transaction.getType();
        // 只允许收入或支出
        if (type != TransactionType.INCOME && type != TransactionType.EXPENSE) {
            log.warn("无效的交易类型：{}", type);
            return false;
        }

        return true;
    }

    /**
     * 计算收支差额
     *
     * @param income 总收入
     * @param expense 总支出
     * @return 差额
     */
    public Money calculateBalance(Money income, Money expense) {
        if (income == null) {
            income = new Money(BigDecimal.ZERO);
        }
        if (expense == null) {
            expense = new Money(BigDecimal.ZERO);
        }

        return new Money(income.toBigDecimal().subtract(expense.toBigDecimal()));
    }

    /**
     * 计算交易列表总金额
     *
     * @param transactions 交易列表
     * @param type 交易类型（null 表示全部）
     * @return 总金额
     */
    public Money calculateTotalAmount(List<Transaction> transactions, TransactionType type) {
        if (transactions == null || transactions.isEmpty()) {
            return new Money(BigDecimal.ZERO);
        }

        BigDecimal total = transactions.stream()
                .filter(t -> type == null || t.getType() == type)
                .map(Transaction::getAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Money(total);
    }

    /**
     * 验证交易描述
     *
     * @param description 描述
     * @return true/false
     */
    public boolean validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            log.warn("交易描述不能为空");
            return false;
        }

        if (description.length() > 500) {
            log.warn("交易描述过长");
            return false;
        }

        return true;
    }
}
