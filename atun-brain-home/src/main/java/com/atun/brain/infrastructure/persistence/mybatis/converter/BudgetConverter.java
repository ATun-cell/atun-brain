package com.atun.brain.infrastructure.persistence.mybatis.converter;

import com.atun.brain.domain.finance.aggregate.Budget;
import com.atun.brain.domain.finance.valueobject.Money;
import com.atun.brain.infrastructure.persistence.mybatis.po.BudgetPO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 预算聚合根与PO之间的转换器
 *
 * @author lij
 * @date 2026/02/03
 */
@Component
public class BudgetConverter {
    
    /**
     * 领域模型 -> PO（用于持久化）
     */
    public BudgetPO toPO(Budget domain) {
        if (domain == null) {
            return null;
        }
        
        BudgetPO po = new BudgetPO();
        po.setId(domain.getId());
        po.setUserId(domain.getUserId());
        po.setCategoryId(domain.getCategory() != null ? domain.getCategory().getId() : null);
        po.setAmount(domain.getAmount().getAmount());
        po.setPeriodType(domain.getPeriodType());
        po.setYear(domain.getYear());
        po.setMonth(domain.getMonth());
        po.setCreatedAt(domain.getCreatedAt());
        po.setUpdatedAt(domain.getUpdatedAt());
        
        return po;
    }
    
    /**
     * PO -> 领域模型（从数据库加载）
     */
    public Budget toDomain(BudgetPO po) {
        if (po == null) {
            return null;
        }
        
        Budget domain = new Budget();
        domain.setId(po.getId());
        domain.setUserId(po.getUserId());
        domain.setAmount(Money.cny(po.getAmount()));
        domain.setPeriodType(po.getPeriodType());
        domain.setYear(po.getYear());
        domain.setMonth(po.getMonth());
        domain.setCreatedAt(po.getCreatedAt());
        domain.setUpdatedAt(po.getUpdatedAt());
        
        return domain;
    }
    
    /**
     * PO列表 -> 领域模型列表
     */
    public List<Budget> toDomainList(List<BudgetPO> pos) {
        if (pos == null) {
            return new ArrayList<>();
        }
        return pos.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}
