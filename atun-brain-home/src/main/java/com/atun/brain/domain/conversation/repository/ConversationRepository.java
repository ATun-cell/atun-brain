package com.atun.brain.domain.conversation.repository;

import com.atun.brain.domain.conversation.aggregate.Conversation;
import com.atun.brain.domain.conversation.valueobject.AgentType;
import com.atun.brain.domain.conversation.valueobject.SessionId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 对话仓储接口
 * 负责Conversation聚合根的持久化操作
 *
 * @author lij
 * @date 2026/02/03
 */
public interface ConversationRepository {
    
    /**
     * 保存会话
     */
    Conversation save(Conversation conversation);
    
    /**
     * 根据ID查找会话
     */
    Optional<Conversation> findById(Long id);
    
    /**
     * 根据会话ID查找
     */
    Optional<Conversation> findBySessionId(SessionId sessionId);
    
    /**
     * 根据用户ID查找所有会话
     */
    List<Conversation> findByUserId(Long userId);
    
    /**
     * 根据用户ID和代理类型查找会话
     */
    List<Conversation> findByUserIdAndAgentType(Long userId, AgentType agentType);
    
    /**
     * 查找用户的活跃会话（未归档且最近30分钟内活跃）
     */
    List<Conversation> findActiveConversations(Long userId);
    
    /**
     * 查找用户的归档会话
     */
    List<Conversation> findArchivedConversations(Long userId);
    
    /**
     * 查找指定时间范围内的会话
     */
    List<Conversation> findByUserIdAndTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 更新会话
     */
    void update(Conversation conversation);
    
    /**
     * 删除会话
     */
    void deleteById(Long id);
    
    /**
     * 统计用户的会话数量
     */
    long countByUserId(Long userId);
    
    /**
     * 清理过期的已归档会话（超过指定天数）
     */
    int deleteArchivedConversationsOlderThan(int days);
}
