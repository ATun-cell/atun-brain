package com.atun.brain.home.infrastructure.persistence.mapper;

import com.atun.brain.home.infrastructure.persistence.po.ChatMemoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 会话记忆 MyBatis Mapper
 *
 * @author atun-brain
 * @since 1.0
 */
@Mapper
public interface ChatMemoryMapper {

    /**
     * 根据 memoryId 查询记忆
     *
     * @param memoryId 记忆标识
     * @return 记忆 PO
     */
    ChatMemoryPO findByMemoryId(@Param("memoryId") String memoryId);

    /**
     * 插入记忆
     *
     * @param po 记忆 PO
     * @return 影响行数
     */
    int insert(ChatMemoryPO po);

    /**
     * 更新记忆
     *
     * @param memoryId 记忆标识
     * @param messagesJson 消息 JSON
     * @return 影响行数
     */
    int updateMemory(@Param("memoryId") String memoryId, @Param("messagesJson") String messagesJson);

    /**
     * 删除记忆
     *
     * @param memoryId 记忆标识
     * @return 影响行数
     */
    int deleteByMemoryId(@Param("memoryId") String memoryId);
}
