package com.atun.brain.home.tool.memory;

import com.atun.brain.agent.tools.spi.ToolProvider;
import com.atun.brain.agent.tools.spi.ToolResult;
import com.atun.brain.home.application.service.MemoryAppService;
import com.atun.brain.home.application.service.dto.MemoryResponse;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.V;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 记忆工具提供者
 * 实现 ToolProvider SPI，将记忆工具注册到 Agent Pipeline
 *
 * @author atun-brain
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class MemoryToolProvider implements ToolProvider {

    private final MemoryAppService memoryAppService;

    @Override
    public String getGroupName() {
        return "memory-tools";
    }

    @Override
    public String getDescription() {
        return "个人记忆仓库工具集，支持记忆保存、检索、摘要等功能";
    }

    @Override
    public List<Object> getToolObjects() {
        return List.of(new MemoryTools());
    }

    @Override
    public int getOrder() {
        return 90;
    }

    /**
     * 记忆工具内部类
     */
    public class MemoryTools {

        /**
         * 从用户的个人记忆仓库中检索相关信息
         */
        @Tool("根据关键词或问题从用户的个人记忆仓库中检索相关信息。" +
              "参数：userId-用户 ID，query-查询关键词，maxResults-最大返回数量 (可选，默认 10)")
        public ToolResult searchMemories(
                @V("userId") Long userId,
                @V("query") String query,
                @V("maxResults") Integer maxResults
        ) {
            long start = System.currentTimeMillis();
            try {
                List<MemoryResponse> memories = memoryAppService.searchMemories(userId, query, maxResults);

                return ToolResult.success("searchMemories",
                        "找到" + memories.size() + "条相关记忆：" + memories,
                        System.currentTimeMillis() - start);
            } catch (Exception e) {
                return ToolResult.failure("searchMemories",
                        e.getMessage(),
                        System.currentTimeMillis() - start);
            }
        }

        /**
         * 获取用户最近几天的记忆摘要
         */
        @Tool("获取用户最近几天的记忆摘要，用于上下文参考。" +
              "参数：userId-用户 ID，days-天数 (可选，默认 7)")
        public ToolResult getMemorySummary(
                @V("userId") Long userId,
                @V("days") Integer days
        ) {
            long start = System.currentTimeMillis();
            try {
                int d = days != null ? days : 7;
                String summary = memoryAppService.getMemorySummary(userId, d);

                return ToolResult.success("getMemorySummary",
                        summary,
                        System.currentTimeMillis() - start);
            } catch (Exception e) {
                return ToolResult.failure("getMemorySummary",
                        e.getMessage(),
                        System.currentTimeMillis() - start);
            }
        }

        /**
         * 保存记忆到记忆仓库
         */
        @Tool("当用户提到重要事件、新知识或需要记录的信息时，自动保存到记忆仓库。" +
              "参数：userId-用户 ID，type-记忆类型 (CONVERSATION/EVENT/KNOWLEDGE/PERSON/PLACE/MEDIA)，" +
              "content-记忆内容，title-记忆标题 (可选)，isImportant-是否重要 (可选)")
        public ToolResult saveMemory(
                @V("userId") Long userId,
                @V("type") String type,
                @V("content") String content,
                @V("title") String title,
                @V("isImportant") Boolean isImportant
        ) {
            long start = System.currentTimeMillis();
            try {
                MemoryResponse response = memoryAppService.saveMemory(
                        userId, type, title, content, null, isImportant);

                return ToolResult.success("saveMemory",
                        "已成功保存记忆：" + title,
                        System.currentTimeMillis() - start);
            } catch (Exception e) {
                return ToolResult.failure("saveMemory",
                        e.getMessage(),
                        System.currentTimeMillis() - start);
            }
        }
    }
}
