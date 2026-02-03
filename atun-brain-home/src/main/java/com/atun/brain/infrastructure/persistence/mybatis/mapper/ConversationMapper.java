package com.atun.brain.infrastructure.persistence.mybatis.mapper;

import com.atun.brain.infrastructure.persistence.mybatis.po.ConversationPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConversationMapper {
    
    ConversationPO findById(@Param("id") Long id);
    
    ConversationPO findBySessionId(@Param("sessionId") String sessionId);
    
    List<ConversationPO> findByUserId(@Param("userId") Long userId);
    
    List<ConversationPO> findByUserIdAndAgentType(@Param("userId") Long userId,
            @Param("agentType") String agentType);
    
    int insert(ConversationPO conversation);
    
    int update(ConversationPO conversation);
    
    int updateLastActiveAt(@Param("id") Long id);
    
    int deleteById(@Param("id") Long id);
}
