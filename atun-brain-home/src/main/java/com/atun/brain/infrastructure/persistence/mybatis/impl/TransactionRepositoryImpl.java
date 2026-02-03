package com.atun.brain.infrastructure.persistence.mybatis.impl;

import com.atun.brain.domain.finance.aggregate.Transaction;
import com.atun.brain.domain.finance.repository.TransactionRepository;
import com.atun.brain.domain.finance.valueobject.Period;
import com.atun.brain.infrastructure.persistence.mybatis.converter.TransactionConverter;
import com.atun.brain.infrastructure.persistence.mybatis.mapper.TransactionMapper;
import com.atun.brain.infrastructure.persistence.mybatis.po.TransactionPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 交易仓储实现类 - 基础设施层
 * 使用MyBatis进行数据持久化
 *
 * @author lij
 * @date 2026/02/03
 */
@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {
    
    private final TransactionMapper transactionMapper;
    private final TransactionConverter transactionConverter;
    
    @Override
    public Transaction save(Transaction transaction) {
        TransactionPO po = transactionConverter.toPO(transaction);
        
        if (transaction.isNew()) {
            transactionMapper.insert(po);
            transaction.setId(po.getId());
        } else {
            transactionMapper.update(po);
        }
        
        return transaction;
    }
    
    @Override
    public Optional<Transaction> findById(Long id) {
        TransactionPO po = transactionMapper.findById(id);
        return Optional.ofNullable(transactionConverter.toDomain(po));
    }
    
    @Override
    public List<Transaction> findByUserId(Long userId) {
        List<TransactionPO> pos = transactionMapper.findByUserId(userId);
        return transactionConverter.toDomainList(pos);
    }
    
    @Override
    public List<Transaction> findByUserIdAndPeriod(Long userId, Period period) {
        List<TransactionPO> pos = 
            transactionMapper.findByUserIdAndDateRange(userId, period.getStartDate(), period.getEndDate());
        return transactionConverter.toDomainList(pos);
    }
    
    @Override
    public List<Transaction> findByUserIdAndCategoryId(Long userId, Long categoryId) {
        List<TransactionPO> pos = 
            transactionMapper.findByUserIdAndCategoryId(userId, categoryId);
        return transactionConverter.toDomainList(pos);
    }
    
    @Override
    public List<Transaction> findByUserIdWithCategory(Long userId, Period period) {
        // 通过两步查询实现：先查Transaction，再关联Category
        List<TransactionPO> pos = 
            transactionMapper.findByUserIdAndDateRange(userId, period.getStartDate(), period.getEndDate());
        return transactionConverter.toDomainList(pos);
    }
    
    @Override
    public void deleteById(Long id) {
        transactionMapper.deleteById(id);
    }
    
    @Override
    public Transaction update(Transaction transaction) {
        return save(transaction);
    }
}
