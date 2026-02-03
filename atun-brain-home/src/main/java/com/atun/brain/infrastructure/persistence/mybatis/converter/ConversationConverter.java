package com.atun.brain.infrastructure.persistence.mybatis.converter;

import com.atun.brain.domain.conversation.aggregate.Conversation;
import com.atun.brain.domain.conversation.valueobject.AgentType;
import com.atun.brain.domain.conversation.valueobject.MessageRole;
import com.atun.brain.domain.conversation.valueobject.SessionId;
import com.atun.brain.infrastructure.persistence.mybatis.po.ConversationPO;
import com.atun.brain.infrastructure.persistence.mybatis.po.MessagePO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 会话转换器
 * 负责Conversation领域对象与ConversationPO之间的转换
 *
 * @author lij
 * @date 2026/02/03
 */
@Component
public class ConversationConverter {
    
    /**
     * 将领域对象转换为PO
     */
    public ConversationPO toPO(Conversation conversation) {
        if (conversation == null) {
            return null;
        }
        
        ConversationPO po = new ConversationPO();
        po.setId(conversation.getId());
        po.setUserId(conversation.getUserId());
        po.setSessionId(conversation.getSessionId().getValue());
        po.setAgentType(conversation.getAgentType().getCode());
        po.setLastActiveAt(conversation.getLastActiveAt());
        po.setArchived(conversation.isArchived());
        po.setTitle(conversation.getTitle());
        po.setCreatedAt(conversation.getCreatedAt());
        po.setUpdatedAt(conversation.getUpdatedAt());
        
        return po;
    }
    
    /**
     * 将PO转换为领域对象（不含消息）
     */
    public Conversation toDomain(ConversationPO po) {
        if (po == null) {
            return null;
        }
        
        Conversation conversation = new Conversation();
        conversation.setId(po.getId());
        conversation.setUserId(po.getUserId());
        conversation.setSessionId(SessionId.from(po.getSessionId()));
        conversation.setAgentType(AgentType.fromCode(po.getAgentType()));
        conversation.setLastActiveAt(po.getLastActiveAt());
        conversation.setArchived(po.getArchived() != null ? po.getArchived() : false);
        conversation.setTitle(po.getTitle());
        conversation.setCreatedAt(po.getCreatedAt());
        conversation.setUpdatedAt(po.getUpdatedAt());
        
        return conversation;
    }
    
    /**
     * 将PO和MessagePO列表转换为完整的领域对象
     */
    public Conversation toDomainWithMessages(ConversationPO po, List<MessagePO> messagePOs) {
        Conversation conversation = toDomain(po);
        if (conversation == null) {
            return null;
        }
        
        if (messagePOs != null && !messagePOs.isEmpty()) {
            for (MessagePO messagePO : messagePOs) {
                MessageRole role = MessageRole.fromCode(messagePO.getRole());
                conversation.addMessage(role, messagePO.getContent(), messagePO.getMetadata());
            }
        }
        
        return conversation;
    }
    
    /**
     * 将Message转换为MessagePO
     */
    public MessagePO messageToPO(Conversation.Message message, Long conversationId) {
        if (message == null) {
            return null;
        }
        
        MessagePO po = new MessagePO();
        po.setConversationId(conversationId);
        po.setRole(message.getRole().getCode());
        po.setContent(message.getContent());
        po.setMetadata(message.getMetadata());
        po.setTimestamp(message.getTimestamp());
        po.setCreatedAt(message.getTimestamp());
        
        return po;
    }
    
    /**
     * 批量转换Messages
     */
    public List<MessagePO> messagesToPOs(List<Conversation.Message> messages, Long conversationId) {
        if (messages == null) {
            return null;
        }
        
        return messages.stream()
                .map(message -> messageToPO(message, conversationId))
                .collect(Collectors.toList());
    }
}
