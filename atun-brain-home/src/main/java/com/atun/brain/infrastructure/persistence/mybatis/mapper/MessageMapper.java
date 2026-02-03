package com.atun.brain.infrastructure.persistence.mybatis.mapper;

import com.atun.brain.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    
    Message findById(@Param("id") Long id);
    
    List<Message> findByConversationId(@Param("conversationId") Long conversationId);
    
    List<Message> findByConversationIdWithLimit(@Param("conversationId") Long conversationId,
            @Param("limit") Integer limit);
    
    int insert(Message message);
    
    int deleteByConversationId(@Param("conversationId") Long conversationId);
}
