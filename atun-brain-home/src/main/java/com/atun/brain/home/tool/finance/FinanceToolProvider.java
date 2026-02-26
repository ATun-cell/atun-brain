package com.atun.brain.home.tool.finance;

import com.atun.brain.agent.tools.spi.ToolProvider;
import com.atun.brain.agent.tools.spi.ToolResult;
import com.atun.brain.home.application.service.TransactionAppService;
import com.atun.brain.home.application.service.dto.TransactionResponse;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.V;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 财务工具提供者
 * 实现 ToolProvider SPI，将财务工具注册到 Agent Pipeline
 *
 * @author atun-brain
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class FinanceToolProvider implements ToolProvider {

    private final TransactionAppService transactionAppService;

    @Override
    public String getGroupName() {
        return "finance-tools";
    }

    @Override
    public String getDescription() {
        return "个人记账和财务分析工具集，支持快速记账、查询交易、统计收支等功能";
    }

    @Override
    public List<Object> getToolObjects() {
        return List.of(new FinanceTools());
    }

    @Override
    public int getOrder() {
        return 100;
    }

    /**
     * 财务工具内部类
     * 包含所有 @Tool 标注的方法
     */
    public class FinanceTools {

        /**
         * 记录用户的收支交易
         */
        @Tool("记录用户的收支交易，支持自动分类。" +
              "参数：userId-用户 ID，amount-金额，type-类型 (INCOME 收入/EXPENSE 支出)，" +
              "category-分类名称，description-描述")
        public ToolResult recordTransaction(
                @V("userId") Long userId,
                @V("amount") BigDecimal amount,
                @V("type") String type,
                @V("category") String category,
                @V("description") String description
        ) {
            long start = System.currentTimeMillis();
            try {
                // 根据分类名称查找分类 ID（简化处理，暂时传 null）
                Long categoryId = null;

                TransactionResponse response = transactionAppService.createTransaction(
                        userId, type, amount, categoryId, description, LocalDateTime.now());

                return ToolResult.success("recordTransaction",
                        "已成功记录：" + description + "，金额：" + amount + "元",
                        System.currentTimeMillis() - start);
            } catch (Exception e) {
                return ToolResult.failure("recordTransaction",
                        e.getMessage(),
                        System.currentTimeMillis() - start);
            }
        }

        /**
         * 查询用户的交易记录
         */
        @Tool("查询用户的交易记录，支持按时间、类型筛选。" +
              "参数：userId-用户 ID，startTime-开始时间 (yyyy-MM-dd)，" +
              "endTime-结束时间 (yyyy-MM-dd)，type-类型 (可选)")
        public ToolResult queryTransactions(
                @V("userId") Long userId,
                @V("startTime") String startTime,
                @V("endTime") String endTime,
                @V("type") String type
        ) {
            long start = System.currentTimeMillis();
            try {
                List<TransactionResponse> transactions = transactionAppService.listTransactions(
                        userId,
                        parseDate(startTime),
                        parseDate(endTime),
                        type,
                        null);

                return ToolResult.success("queryTransactions",
                        transactions.toString(),
                        System.currentTimeMillis() - start);
            } catch (Exception e) {
                return ToolResult.failure("queryTransactions",
                        e.getMessage(),
                        System.currentTimeMillis() - start);
            }
        }

        /**
         * 获取用户的收支统计
         */
        @Tool("获取用户的收支统计摘要，包括总支出、总收入、结余等。" +
              "参数：userId-用户 ID，startTime-开始时间 (yyyy-MM-dd)，endTime-结束时间 (yyyy-MM-dd)")
        public ToolResult getStatistics(
                @V("userId") Long userId,
                @V("startTime") String startTime,
                @V("endTime") String endTime
        ) {
            long start = System.currentTimeMillis();
            try {
                var stats = transactionAppService.getStatistics(userId, startTime, endTime);

                return ToolResult.success("getStatistics",
                        String.format("总收入：%s元，总支出：%s元，结余：%s元，交易笔数：%d",
                                stats.getTotalIncome(),
                                stats.getTotalExpense(),
                                stats.getBalance(),
                                stats.getTransactionCount()),
                        System.currentTimeMillis() - start);
            } catch (Exception e) {
                return ToolResult.failure("getStatistics",
                        e.getMessage(),
                        System.currentTimeMillis() - start);
            }
        }

        private LocalDateTime parseDate(String dateStr) {
            if (dateStr == null || dateStr.trim().isEmpty()) {
                return null;
            }
            try {
                return LocalDateTime.parse(dateStr + "T00:00:00");
            } catch (Exception e) {
                throw new IllegalArgumentException("日期格式错误，应为 yyyy-MM-dd");
            }
        }
    }
}
