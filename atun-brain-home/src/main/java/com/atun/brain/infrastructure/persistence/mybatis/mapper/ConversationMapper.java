package com.atun.brain.infrastructure.persistence.mybatis.mapper;

import com.atun.brain.entity.Conversation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConversationMapper {
    
    Conversation findById(@Param("id") Long id);
    
    Conversation findBySessionId(@Param("sessionId") String sessionId);
    
    List<Conversation> findByUserId(@Param("userId") Long userId);
    
    List<Conversation> findByUserIdAndAgentType(@Param("userId") Long userId,
            @Param("agentType") String agentType);
    
    int insert(Conversation conversation);
    
    int update(Conversation conversation);
    
    int updateLastActiveAt(@Param("id") Long id);
}
