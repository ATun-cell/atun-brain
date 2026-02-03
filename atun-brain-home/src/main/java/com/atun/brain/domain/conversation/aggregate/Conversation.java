package com.atun.brain.domain.conversation.aggregate;

import com.atun.brain.domain.shared.BaseEntity;
import com.atun.brain.domain.conversation.valueobject.AgentType;
import com.atun.brain.domain.conversation.valueobject.MessageRole;
import com.atun.brain.domain.conversation.valueobject.SessionId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 对话聚合根 包含message列表
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Conversation extends BaseEntity {
    
    /** 用户ID */
    private Long userId;
    
    /** 会话ID */
    private SessionId sessionId;
    
    /** 代理类型 */
    private AgentType agentType;
    
    /** 消息列表 */
    private List<Message> messages;
    
    /** 最后活跃时间 */
    private LocalDateTime lastActiveAt;
    
    /** 是否已归档 */
    private boolean archived;
    
    /** 会话标题（可选，从第一条消息提取） */
    private String title;
    
    /**
     * 构造方法
     */
    public Conversation() {
        super();
        this.messages = new ArrayList<>();
        this.lastActiveAt = LocalDateTime.now();
        this.archived = false;
    }
    
    /**
     * 创建新会话
     */
    public static Conversation create(Long userId, AgentType agentType) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (agentType == null) {
            throw new IllegalArgumentException("代理类型不能为空");
        }
        
        Conversation conversation = new Conversation();
        conversation.setUserId(userId);
        conversation.setSessionId(SessionId.generate());
        conversation.setAgentType(agentType);
        return conversation;
    }
    
    /**
     * 添加消息
     */
    public void addMessage(MessageRole role, String content) {
        if (role == null || content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("消息角色和内容不能为空");
        }
        if (archived) {
            throw new IllegalStateException("已归档的会话不能添加消息");
        }
        
        Message message = new Message(role, content, LocalDateTime.now());
        this.messages.add(message);
        this.lastActiveAt = LocalDateTime.now();
        
        // 如果是第一条用户消息，设置为标题
        if (title == null && role.isUser()) {
            this.title = content.length() > 50 ? content.substring(0, 50) + "..." : content;
        }
    }
    
    /**
     * 添加消息（带元数据）
     */
    public void addMessage(MessageRole role, String content, String metadata) {
        if (role == null || content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("消息角色和内容不能为空");
        }
        if (archived) {
            throw new IllegalStateException("已归档的会话不能添加消息");
        }
        
        Message message = new Message(role, content, metadata, LocalDateTime.now());
        this.messages.add(message);
        this.lastActiveAt = LocalDateTime.now();
        
        if (title == null && role.isUser()) {
            this.title = content.length() > 50 ? content.substring(0, 50) + "..." : content;
        }
    }
    
    /**
     * 获取所有消息（不可修改）
     */
    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }
    
    /**
     * 获取最近的N条消息
     */
    public List<Message> getRecentMessages(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("消息数量必须大于0");
        }
        
        int size = messages.size();
        int fromIndex = Math.max(0, size - count);
        return messages.subList(fromIndex, size);
    }
    
    /**
     * 获取用户消息列表
     */
    public List<Message> getUserMessages() {
        return messages.stream()
                .filter(m -> m.getRole().isUser())
                .collect(Collectors.toList());
    }
    
    /**
     * 获取助手消息列表
     */
    public List<Message> getAssistantMessages() {
        return messages.stream()
                .filter(m -> m.getRole().isAssistant())
                .collect(Collectors.toList());
    }
    
    /**
     * 获取消息数量
     */
    public int getMessageCount() {
        return messages.size();
    }
    
    /**
     * 判断会话是否活跃（30分钟内有消息）
     */
    public boolean isActive() {
        if (archived) {
            return false;
        }
        return lastActiveAt != null && 
               lastActiveAt.isAfter(LocalDateTime.now().minusMinutes(30));
    }
    
    /**
     * 归档会话
     */
    public void archive() {
        this.archived = true;
    }
    
    /**
     * 取消归档
     */
    public void unarchive() {
        this.archived = false;
    }
    
    /**
     * 设置标题
     */
    public void setCustomTitle(String title) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title;
        }
    }
    
    /**
     * 清空消息（谨慎使用）
     */
    public void clearMessages() {
        this.messages.clear();
    }
    
    /**
     * 消息实体（内部类）
     */
    @Value
    public static class Message {
        MessageRole role;
        String content;
        String metadata;
        LocalDateTime timestamp;
        
        public Message(MessageRole role, String content, LocalDateTime timestamp) {
            this(role, content, null, timestamp);
        }
        
        public Message(MessageRole role, String content, String metadata, LocalDateTime timestamp) {
            if (role == null) {
                throw new IllegalArgumentException("消息角色不能为空");
            }
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("消息内容不能为空");
            }
            this.role = role;
            this.content = content;
            this.metadata = metadata;
            this.timestamp = timestamp;
        }
    }
}
