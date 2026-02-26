package com.atun.brain.home.infrastructure.persistence.mapper;

import com.atun.brain.home.infrastructure.persistence.po.MemoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 记忆 MyBatis Mapper
 *
 * @author atun-brain
 * @since 1.0
 */
@Mapper
public interface MemoryMapper {

    /**
     * 插入记忆
     *
     * @param po 记忆 PO
     * @return 影响行数
     */
    int insert(MemoryPO po);

    /**
     * 根据 ID 查询记忆
     *
     * @param id 记忆 ID
     * @return 记忆 PO
     */
    MemoryPO selectById(@Param("id") Long id);

    /**
     * 根据用户 ID 查询记忆列表
     *
     * @param userId 用户 ID
     * @return 记忆列表
     */
    List<MemoryPO> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据类型查询记忆
     *
     * @param userId 用户 ID
     * @param type 类型
     * @return 记忆列表
     */
    List<MemoryPO> selectByUserIdAndType(
            @Param("userId") Long userId,
            @Param("type") String type);

    /**
     * 根据时间范围查询记忆
     *
     * @param userId 用户 ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 记忆列表
     */
    List<MemoryPO> selectByUserIdAndTimeRange(
            @Param("userId") Long userId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 根据会话 ID 查询记忆
     *
     * @param userId 用户 ID
     * @param sessionId 会话 ID
     * @return 记忆列表
     */
    List<MemoryPO> selectByUserIdAndSessionId(
            @Param("userId") Long userId,
            @Param("sessionId") String sessionId);

    /**
     * 更新记忆
     *
     * @param po 记忆 PO
     * @return 影响行数
     */
    int update(MemoryPO po);

    /**
     * 逻辑删除记忆
     *
     * @param id 记忆 ID
     * @return 影响行数
     */
    int deleteLogic(@Param("id") Long id);
}
