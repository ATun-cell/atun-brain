package com.atun.brain.infrastructure.persistence.mybatis.impl;

import com.atun.brain.domain.conversation.aggregate.Conversation;
import com.atun.brain.domain.conversation.repository.ConversationRepository;
import com.atun.brain.domain.conversation.valueobject.AgentType;
import com.atun.brain.domain.conversation.valueobject.SessionId;
import com.atun.brain.infrastructure.persistence.mybatis.converter.ConversationConverter;
import com.atun.brain.infrastructure.persistence.mybatis.mapper.ConversationMapper;
import com.atun.brain.infrastructure.persistence.mybatis.mapper.MessageMapper;
import com.atun.brain.infrastructure.persistence.mybatis.po.ConversationPO;
import com.atun.brain.infrastructure.persistence.mybatis.po.MessagePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 会话仓储实现
 *
 * @author lij
 * @date 2026/02/03
 */
@Repository
@RequiredArgsConstructor
public class ConversationRepositoryImpl implements ConversationRepository {
    
    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;
    private final ConversationConverter converter;
    
    @Override
    @Transactional
    public Conversation save(Conversation conversation) {
        ConversationPO po = converter.toPO(conversation);
        
        if (conversation.getId() == null) {
            conversationMapper.insert(po);
            conversation.setId(po.getId());
        } else {
            conversationMapper.update(po);
        }
        
        // 保存消息
        if (conversation.getMessages() != null && !conversation.getMessages().isEmpty()) {
            // 先删除旧消息（简化处理）
            messageMapper.deleteByConversationId(conversation.getId());
            
            // 插入新消息
            List<MessagePO> messagePOs = converter.messagesToPOs(
                    conversation.getMessages(), conversation.getId());
            for (MessagePO messagePO : messagePOs) {
                messageMapper.insert(messagePO);
            }
        }
        
        return conversation;
    }
    
    @Override
    public Optional<Conversation> findById(Long id) {
        ConversationPO po = conversationMapper.findById(id);
        if (po == null) {
            return Optional.empty();
        }
        
        List<MessagePO> messagePOs = messageMapper.findByConversationId(id);
        Conversation conversation = converter.toDomainWithMessages(po, messagePOs);
        return Optional.ofNullable(conversation);
    }
    
    @Override
    public Optional<Conversation> findBySessionId(SessionId sessionId) {
        ConversationPO po = conversationMapper.findBySessionId(sessionId.getValue());
        if (po == null) {
            return Optional.empty();
        }
        
        List<MessagePO> messagePOs = messageMapper.findByConversationId(po.getId());
        Conversation conversation = converter.toDomainWithMessages(po, messagePOs);
        return Optional.ofNullable(conversation);
    }
    
    @Override
    public List<Conversation> findByUserId(Long userId) {
        List<ConversationPO> pos = conversationMapper.findByUserId(userId);
        return pos.stream()
                .map(converter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Conversation> findByUserIdAndAgentType(Long userId, AgentType agentType) {
        List<ConversationPO> pos = conversationMapper.findByUserIdAndAgentType(
                userId, agentType.getCode());
        return pos.stream()
                .map(converter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Conversation> findActiveConversations(Long userId) {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(30);
        List<ConversationPO> pos = conversationMapper.findByUserId(userId);
        
        return pos.stream()
                .map(converter::toDomain)
                .filter(c -> !c.isArchived() && 
                            c.getLastActiveAt() != null && 
                            c.getLastActiveAt().isAfter(threshold))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Conversation> findArchivedConversations(Long userId) {
        List<ConversationPO> pos = conversationMapper.findByUserId(userId);
        return pos.stream()
                .map(converter::toDomain)
                .filter(Conversation::isArchived)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Conversation> findByUserIdAndTimeRange(Long userId, 
            LocalDateTime startTime, LocalDateTime endTime) {
        List<ConversationPO> pos = conversationMapper.findByUserId(userId);
        return pos.stream()
                .map(converter::toDomain)
                .filter(c -> c.getCreatedAt() != null &&
                            c.getCreatedAt().isAfter(startTime) &&
                            c.getCreatedAt().isBefore(endTime))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void update(Conversation conversation) {
        ConversationPO po = converter.toPO(conversation);
        conversationMapper.update(po);
        
        // 更新消息
        if (conversation.getMessages() != null) {
            messageMapper.deleteByConversationId(conversation.getId());
            List<MessagePO> messagePOs = converter.messagesToPOs(
                    conversation.getMessages(), conversation.getId());
            for (MessagePO messagePO : messagePOs) {
                messageMapper.insert(messagePO);
            }
        }
    }
    
    @Override
    @Transactional
    public void deleteById(Long id) {
        messageMapper.deleteByConversationId(id);
        conversationMapper.deleteById(id);
    }
    
    @Override
    public long countByUserId(Long userId) {
        List<ConversationPO> pos = conversationMapper.findByUserId(userId);
        return pos.size();
    }
    
    @Override
    @Transactional
    public int deleteArchivedConversationsOlderThan(int days) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(days);
        // 需要在Mapper中添加对应方法，这里暂时返回0
        // 实际实现需要添加对应的SQL查询
        return 0;
    }
}
