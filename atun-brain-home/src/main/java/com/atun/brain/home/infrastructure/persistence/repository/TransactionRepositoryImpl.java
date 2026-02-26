package com.atun.brain.home.infrastructure.persistence.repository;

import com.atun.brain.home.domain.model.entity.Transaction;
import com.atun.brain.home.domain.model.vo.TransactionType;
import com.atun.brain.home.domain.repository.TransactionRepository;
import com.atun.brain.home.infrastructure.converter.TransactionConverter;
import com.atun.brain.home.infrastructure.persistence.mapper.TransactionMapper;
import com.atun.brain.home.infrastructure.persistence.po.TransactionPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 交易仓储实现
 *
 * @author atun-brain
 * @since 1.0
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionMapper transactionMapper;
    private final TransactionConverter converter;

    @Override
    public Transaction save(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        TransactionPO po = converter.toPO(transaction);

        int result;
        if (transaction.getId() == null) {
            // 新增
            result = transactionMapper.insert(po);
            log.debug("[TransactionRepository] 新增交易，id={}", po.getId());
        } else {
            // 更新
            result = transactionMapper.update(po);
            log.debug("[TransactionRepository] 更新交易，id={}", transaction.getId());
        }

        if (result > 0) {
            return converter.toEntity(po);
        }
        return null;
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        TransactionPO po = transactionMapper.selectById(id);
        if (po == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(converter.toEntity(po));
    }

    @Override
    public List<Transaction> findByUserId(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }

        List<TransactionPO> poList = transactionMapper.selectByUserId(userId);
        if (poList == null || poList.isEmpty()) {
            return new ArrayList<>();
        }

        return poList.stream()
                .map(converter::toEntity)
                .toList();
    }

    @Override
    public List<Transaction> findByUserIdAndTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        if (userId == null) {
            return new ArrayList<>();
        }

        List<TransactionPO> poList = transactionMapper.selectByUserIdAndTimeRange(userId, startTime, endTime);
        if (poList == null || poList.isEmpty()) {
            return new ArrayList<>();
        }

        return poList.stream()
                .map(converter::toEntity)
                .toList();
    }

    @Override
    public List<Transaction> findByUserIdAndCategoryId(Long userId, Long categoryId) {
        if (userId == null || categoryId == null) {
            return new ArrayList<>();
        }

        List<TransactionPO> poList = transactionMapper.selectByUserIdAndCategoryId(userId, categoryId);
        if (poList == null || poList.isEmpty()) {
            return new ArrayList<>();
        }

        return poList.stream()
                .map(converter::toEntity)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            return;
        }

        int result = transactionMapper.deleteLogic(id);
        log.debug("[TransactionRepository] 删除交易，id={}, result={}", id, result);
    }
}
