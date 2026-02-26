package com.atun.brain.home.application.service;

import com.atun.brain.home.application.service.dto.MemoryResponse;

import java.util.List;

/**
 * 记忆应用服务
 *
 * @author atun-brain
 * @since 1.0
 */
public interface MemoryAppService {

    /**
     * 保存记忆
     *
     * @param userId 用户 ID
     * @param type 记忆类型
     * @param title 记忆标题
     * @param content 记忆内容
     * @param sessionId 会话 ID（可选）
     * @param isImportant 是否重要
     * @return 记忆响应
     */
    MemoryResponse saveMemory(Long userId, String type, String title, String content,
                              String sessionId, Boolean isImportant);

    /**
     * 根据 ID 查询记忆
     *
     * @param id 记忆 ID
     * @return 记忆响应
     */
    MemoryResponse getMemoryById(Long id);

    /**
     * 查询用户记忆列表
     *
     * @param userId 用户 ID
     * @param type 记忆类型（可选）
     * @param sessionId 会话 ID（可选）
     * @param days 最近 N 天（可选）
     * @return 记忆列表
     */
    List<MemoryResponse> listMemories(Long userId, String type, String sessionId, Integer days);

    /**
     * 语义搜索记忆
     *
     * @param userId 用户 ID
     * @param query 搜索查询文本
     * @param maxResults 最大返回数量
     * @return 记忆列表
     */
    List<MemoryResponse> searchMemories(Long userId, String query, Integer maxResults);

    /**
     * 删除记忆
     *
     * @param id 记忆 ID
     */
    void deleteMemory(Long id);

    /**
     * 获取记忆摘要
     *
     * @param userId 用户 ID
     * @param days 天数
     * @return 摘要文本
     */
    String getMemorySummary(Long userId, Integer days);
}
