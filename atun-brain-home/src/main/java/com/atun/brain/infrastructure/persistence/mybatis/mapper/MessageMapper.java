package com.atun.brain.infrastructure.persistence.mybatis.mapper;

import com.atun.brain.infrastructure.persistence.mybatis.po.MessagePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    
    MessagePO findById(@Param("id") Long id);
    
    List<MessagePO> findByConversationId(@Param("conversationId") Long conversationId);
    
    List<MessagePO> findByConversationIdWithLimit(@Param("conversationId") Long conversationId,
            @Param("limit") Integer limit);
    
    int insert(MessagePO message);
    
    int deleteByConversationId(@Param("conversationId") Long conversationId);
}
