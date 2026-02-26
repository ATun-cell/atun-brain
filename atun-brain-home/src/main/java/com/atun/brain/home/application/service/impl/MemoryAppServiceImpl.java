package com.atun.brain.home.application.service.impl;

import com.atun.brain.home.application.service.MemoryAppService;
import com.atun.brain.home.application.service.dto.MemoryResponse;
import com.atun.brain.home.domain.model.entity.Memory;
import com.atun.brain.home.domain.model.vo.MemoryType;
import com.atun.brain.home.domain.repository.MemoryRepository;
import com.atun.brain.home.domain.service.MemoryDomainService;
import com.atun.brain.home.infrastructure.converter.MemoryConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 记忆应用服务实现
 *
 * @author atun-brain
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemoryAppServiceImpl implements MemoryAppService {

    private final MemoryRepository memoryRepository;
    private final MemoryDomainService domainService;
    private final MemoryConverter converter;

    @Override
    public MemoryResponse saveMemory(Long userId, String type, String title, String content,
                                      String sessionId, Boolean isImportant) {
        log.info("[保存记忆] 开始处理，userId={}, type={}, title={}, sessionId={}", userId, type, title, sessionId);

        validateMemory(type, title, content);

        MemoryType memoryType = parseMemoryType(type);

        Memory memory = Memory.builder()
                .userId(userId)
                .type(memoryType)
                .title(title)
                .content(content)
                .sessionId(sessionId)
                .isImportant(isImportant != null ? isImportant : false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deleted(false)
                .build();

        // 领域服务：生成摘要
        String summary = domainService.generateSummary(memory);
        memory.setSummary(summary);

        // 领域服务：提取元数据
        String metadata = domainService.extractMetadata(content);
        memory.setMetadata(metadata);

        Memory saved = memoryRepository.save(memory);
        log.info("[保存记忆] 保存成功，memoryId={}", saved.getId());

        return converter.toResponse(saved);
    }

    @Override
    public MemoryResponse getMemoryById(Long id) {
        log.info("[查询记忆] 根据 ID 查询，id={}", id);

        if (id == null) {
            throw new IllegalArgumentException("记忆 ID 不能为空");
        }

        Memory memory = memoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("记忆不存在，id=" + id));

        return converter.toResponse(memory);
    }

    @Override
    public List<MemoryResponse> listMemories(Long userId, String type, String sessionId, Integer days) {
        log.info("[查询记忆列表] userId={}, type={}, sessionId={}, days={}", userId, type, sessionId, days);

        if (userId == null) {
            throw new IllegalArgumentException("用户 ID 不能为空");
        }

        List<Memory> memories;

        if (days != null) {
            LocalDateTime startTime = LocalDateTime.now().minusDays(days);
            memories = memoryRepository.findByUserIdAndTimeRange(userId, startTime, LocalDateTime.now());
        } else if (sessionId != null) {
            memories = memoryRepository.findByUserIdAndSessionId(userId, sessionId);
        } else {
            memories = memoryRepository.findByUserId(userId);
        }

        if (type != null) {
            MemoryType filterType = parseMemoryType(type);
            final MemoryType finalFilterType = filterType;
            memories = memories.stream()
                    .filter(m -> m.getType() == finalFilterType)
                    .collect(Collectors.toList());
        }

        return memories.stream()
                .map(converter::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MemoryResponse> searchMemories(Long userId, String query, Integer maxResults) {
        log.info("[搜索记忆] userId={}, query={}, maxResults={}", userId, query, maxResults);

        if (userId == null || query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("用户 ID 和搜索关键词不能为空");
        }

        int limit = maxResults != null ? maxResults : 10;

        // TODO: 实现语义搜索（使用 RAG）
        // 目前使用简单的关键词搜索
        List<Memory> memories = memoryRepository.findByUserId(userId);

        List<Memory> filtered = memories.stream()
                .filter(m -> (m.getTitle() != null && m.getTitle().contains(query)) ||
                             (m.getContent() != null && m.getContent().contains(query)))
                .limit(limit)
                .collect(Collectors.toList());

        return filtered.stream()
                .map(converter::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMemory(Long id) {
        log.info("[删除记忆] id={}", id);

        if (id == null) {
            throw new IllegalArgumentException("记忆 ID 不能为空");
        }

        memoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("记忆不存在，id=" + id));

        memoryRepository.delete(id);
        log.info("[删除记忆] 删除成功，id={}", id);
    }

    @Override
    public String getMemorySummary(Long userId, Integer days) {
        return null;
    }

    // ==================== 私有方法 ====================

    private void validateMemory(String type, String title, String content) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("记忆类型不能为空");
        }

        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("记忆内容不能为空");
        }

        if (content.length() > 10000) {
            throw new IllegalArgumentException("记忆内容不能超过 10000 字符");
        }
    }

    private MemoryType parseMemoryType(String type) {
        try {
            return MemoryType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("无效的记忆类型：" + type);
        }
    }
}
