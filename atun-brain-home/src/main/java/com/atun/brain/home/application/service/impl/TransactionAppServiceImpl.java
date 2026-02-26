package com.atun.brain.home.application.service.impl;

import com.atun.brain.home.application.service.StatisticsResponse;
import com.atun.brain.home.application.service.TransactionAppService;
import com.atun.brain.home.application.service.dto.TransactionResponse;
import com.atun.brain.home.domain.model.entity.Category;
import com.atun.brain.home.domain.model.entity.Transaction;
import com.atun.brain.home.domain.model.vo.Money;
import com.atun.brain.home.domain.model.vo.TransactionType;
import com.atun.brain.home.domain.repository.CategoryRepository;
import com.atun.brain.home.domain.repository.TransactionRepository;
import com.atun.brain.home.domain.service.TransactionDomainService;
import com.atun.brain.home.infrastructure.converter.TransactionConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 交易应用服务实现
 * 负责应用层业务编排和事务管理
 *
 * @author atun-brain
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionAppServiceImpl implements TransactionAppService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionDomainService domainService;
    private final TransactionConverter converter;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionResponse createTransaction(Long userId, String type, BigDecimal amount,
                                                  Long categoryId, String description, LocalDateTime transactionTime) {
        log.info("[创建交易] 开始处理，userId={}, type={}, amount={}, categoryId={}", userId, type, amount, categoryId);

        // ========== 应用层编排：参数校验 ==========
        validateTransaction(type, amount, description);

        // ========== 应用层编排：构建领域实体 ==========
        Transaction transaction = Transaction.builder()
                .userId(userId)
                .type(parseTransactionType(type))
                .amount(amount)
                .categoryId(categoryId)
                .description(description)
                .transactionTime(transactionTime != null ? transactionTime : LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deleted(false)
                .build();

        // ========== 领域层：业务规则验证 ==========
        domainValidation(transaction);

        // ========== 应用层编排：分类验证 ==========
        String categoryName = null;
        if (categoryId != null) {
            Category category = validateCategoryExists(categoryId);
            if (category != null) {
                if (category.getType() != transaction.getType()) {
                    throw new IllegalArgumentException(
                            String.format("分类类型 (%s) 与交易类型 (%s) 不匹配",
                                    category.getType(), transaction.getType()));
                }
                categoryName = category.getName();
            }
        }

        // ========== 基础设施层：持久化 ==========
        Transaction saved = transactionRepository.save(transaction);
        log.info("[创建交易] 创建成功，transactionId={}", saved.getId());

        return converter.toResponse(saved, categoryName);
    }

    @Override
    public TransactionResponse getTransactionById(Long id) {
        log.info("[查询交易] 根据 ID 查询，id={}", id);

        if (id == null) {
            throw new IllegalArgumentException("交易 ID 不能为空");
        }

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("交易不存在，id=" + id));

        String categoryName = null;
        if (transaction.getCategoryId() != null) {
            Category category = categoryRepository.findById(transaction.getCategoryId())
                    .orElse(null);
            if (category != null) {
                categoryName = category.getName();
            }
        }

        return converter.toResponse(transaction, categoryName);
    }

    @Override
    public List<TransactionResponse> listTransactions(Long userId, LocalDateTime startTime,
                                                       LocalDateTime endTime, String type, Long categoryId) {
        log.info("[查询交易列表] userId={}, startTime={}, endTime={}, type={}, categoryId={}",
                userId, startTime, endTime, type, categoryId);

        if (userId == null) {
            throw new IllegalArgumentException("用户 ID 不能为空");
        }

        if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("开始时间不能晚于结束时间");
        }

        List<Transaction> transactions;
        if (startTime != null && endTime != null) {
            transactions = transactionRepository.findByUserIdAndTimeRange(userId, startTime, endTime);
        } else {
            transactions = transactionRepository.findByUserId(userId);
        }

        if (type != null) {
            TransactionType filterType = TransactionType.valueOf(type.toUpperCase());
            transactions = transactions.stream()
                    .filter(t -> t.getType() == filterType)
                    .collect(Collectors.toList());
        }

        if (categoryId != null) {
            transactions = transactions.stream()
                    .filter(t -> categoryId.equals(t.getCategoryId()))
                    .collect(Collectors.toList());
        }

        return transactions.stream()
                .map(t -> {
                    String categoryName = null;
                    if (t.getCategoryId() != null) {
                        Optional<Category> category = categoryRepository.findById(t.getCategoryId());
                        categoryName = category.map(Category::getName).orElse(null);
                    }
                    return converter.toResponse(t, categoryName);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTransaction(Long id) {
        log.info("[删除交易] id={}", id);

        if (id == null) {
            throw new IllegalArgumentException("交易 ID 不能为空");
        }

        transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("交易不存在，id=" + id));

        transactionRepository.delete(id);
        log.info("[删除交易] 删除成功，id={}", id);
    }

    @Override
    public StatisticsResponse getStatistics(Long userId, String startTime, String endTime) {
        log.info("[获取统计] userId={}, startTime={}, endTime={}", userId, startTime, endTime);

        if (userId == null) {
            throw new IllegalArgumentException("用户 ID 不能为空");
        }

        LocalDateTime start = parseDateTime(startTime);
        LocalDateTime end = parseDateTime(endTime);

        List<Transaction> transactions = transactionRepository.findByUserIdAndTimeRange(userId, start, end);

        Money totalIncome = domainService.calculateTotalAmount(transactions, TransactionType.INCOME);
        Money totalExpense = domainService.calculateTotalAmount(transactions, TransactionType.EXPENSE);
        Money balance = domainService.calculateBalance(totalIncome, totalExpense);

        return StatisticsResponse.builder()
                .totalIncome(totalIncome.toBigDecimal())
                .totalExpense(totalExpense.toBigDecimal())
                .balance(balance.toBigDecimal())
                .transactionCount(transactions.size())
                .build();
    }

    // ==================== 私有方法 ====================

    private void validateTransaction(String type, BigDecimal amount, String description) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("交易类型不能为空");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("交易金额必须大于 0");
        }

        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("交易描述不能超过 500 字符");
        }
    }

    private void domainValidation(Transaction transaction) {
        if (!domainService.validateAmount(transaction)) {
            throw new IllegalArgumentException("交易金额验证失败");
        }

        if (!domainService.validateTransactionType(transaction)) {
            throw new IllegalArgumentException("交易类型验证失败");
        }
    }

    private Category validateCategoryExists(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("分类不存在，categoryId=" + categoryId));
    }

    private TransactionType parseTransactionType(String type) {
        try {
            return TransactionType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("无效的交易类型：" + type + "，支持：INCOME, EXPENSE");
        }
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            try {
                LocalDate date = LocalDate.parse(dateTimeStr, DATE_FORMATTER);
                return date.atStartOfDay();
            } catch (Exception e2) {
                throw new IllegalArgumentException("时间格式错误：" + dateTimeStr);
            }
        }
    }
}
