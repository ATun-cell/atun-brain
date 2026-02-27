package com.atun.brain.home.memory.spi;

import com.atun.brain.agent.core.exception.MemoryStorageException;
import com.atun.brain.agent.memory.spi.ChatMemoryStore;
import com.atun.brain.home.infrastructure.persistence.mapper.ChatMemoryMapper;
import com.atun.brain.home.infrastructure.persistence.po.ChatMemoryPO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基于 JDBC 的 ChatMemoryStore 实现
 * <p>
 * 将对话消息以 JSON 序列化方式存储到 MySQL 的 t_chat_memory 表中。
 * 线程安全（依赖 MyBatis 的线程安全性）。
 *
 * @author atun-brain
 * @since 1.0
 */
@Slf4j
@Component
public class JdbcChatMemoryStore implements ChatMemoryStore {

    private final ChatMemoryMapper chatMemoryMapper;
    private final ObjectMapper objectMapper;

    public JdbcChatMemoryStore(ChatMemoryMapper chatMemoryMapper, ObjectMapper objectMapper) {
        this.chatMemoryMapper = chatMemoryMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String id = memoryId.toString();
        try {
            ChatMemoryPO po = chatMemoryMapper.findByMemoryId(id);
            if (po == null || po.getMessagesJson() == null) {
                return new ArrayList<>();
            }
            return deserializeMessages(po.getMessagesJson());
        } catch (JsonProcessingException e) {
            log.error("反序列化消息失败：memoryId={}", id, e);
            throw new MemoryStorageException("消息格式错误：" + id, e);
        } catch (Exception e) {
            log.error("获取记忆失败：memoryId={}", id, e);
            throw new MemoryStorageException("获取记忆失败：" + id, e);
        }
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String id = memoryId.toString();
        try {
            String json = serializeMessages(messages);
            ChatMemoryPO existing = chatMemoryMapper.findByMemoryId(id);

            if (existing == null) {
                ChatMemoryPO newPo = ChatMemoryPO.builder()
                        .memoryId(id)
                        .messagesJson(json)
                        .build();
                chatMemoryMapper.insert(newPo);
            } else {
                chatMemoryMapper.updateMemory(id, json);
            }
            log.debug("更新记忆成功：memoryId={}, messageCount={}", id, messages.size());
        } catch (JsonProcessingException e) {
            log.error("序列化消息失败：memoryId={}", id, e);
            throw new MemoryStorageException("消息序列化失败：" + id, e);
        } catch (Exception e) {
            log.error("更新记忆失败：memoryId={}", id, e);
            throw new MemoryStorageException("更新记忆失败：" + id, e);
        }
    }

    @Override
    public void deleteMessages(Object memoryId) {
        String id = memoryId.toString();
        chatMemoryMapper.deleteByMemoryId(id);
        log.debug("删除记忆：memoryId={}", id);
    }

    /**
     * 序列化消息列表为 JSON
     */
    private String serializeMessages(List<ChatMessage> messages) throws JsonProcessingException {
        List<Map<String, String>> serialized = messages.stream()
                .map(msg -> {
                    String role, content;
                    if (msg instanceof SystemMessage sm) {
                        role = "system";
                        content = sm.text();
                    } else if (msg instanceof UserMessage um) {
                        role = "user";
                        content = um.singleText();
                    } else if (msg instanceof AiMessage am) {
                        role = "assistant";
                        content = am.text();
                    } else {
                        role = "unknown";
                        content = msg.toString();
                    }
                    return Map.of("role", role, "content", content);
                })
                .toList();
        return objectMapper.writeValueAsString(serialized);
    }

    /**
     * 反序列化 JSON 为消息列表
     */
    private List<ChatMessage> deserializeMessages(String json) throws JsonProcessingException {
        List<Map<String, String>> serialized = objectMapper.readValue(
                json, new TypeReference<List<Map<String, String>>>() {}
        );

        List<ChatMessage> messages = new ArrayList<>();
        for (Map<String, String> entry : serialized) {
            String role = entry.get("role");
            String content = entry.get("content");

            switch (role) {
                case "system" -> messages.add(SystemMessage.from(content));
                case "user" -> messages.add(UserMessage.from(content));
                case "assistant" -> messages.add(AiMessage.from(content));
                default -> log.warn("未知的消息角色：{}", role);
            }
        }
        return messages;
    }
}
