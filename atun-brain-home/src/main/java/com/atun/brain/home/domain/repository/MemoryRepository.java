package com.atun.brain.home.domain.repository;

import com.atun.brain.home.domain.model.entity.Memory;
import com.atun.brain.home.domain.model.vo.MemoryType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 记忆仓储接口
 *
 * @author atun-brain
 * @since 1.0
 */
public interface MemoryRepository {

    /**
     * 保存记忆
     *
     * @param memory 记忆实体
     * @return 保存后的实体
     */
    Memory save(Memory memory);

    /**
     * 根据 ID 查找记忆
     *
     * @param id 记忆 ID
     * @return 记忆实体
     */
    Optional<Memory> findById(Long id);

    /**
     * 根据用户 ID 查找记忆列表
     *
     * @param userId 用户 ID
     * @return 记忆列表
     */
    List<Memory> findByUserId(Long userId);

    /**
     * 根据类型查询记忆
     *
     * @param userId 用户 ID
     * @param type 记忆类型
     * @return 记忆列表
     */
    List<Memory> findByUserIdAndType(Long userId, MemoryType type);

    /**
     * 根据时间范围查询记忆
     *
     * @param userId 用户 ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 记忆列表
     */
    List<Memory> findByUserIdAndTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据会话 ID 查询记忆
     *
     * @param userId 用户 ID
     * @param sessionId 会话 ID
     * @return 记忆列表
     */
    List<Memory> findByUserIdAndSessionId(Long userId, String sessionId);

    /**
     * 删除记忆（逻辑删除）
     *
     * @param id 记忆 ID
     */
    void delete(Long id);
}
