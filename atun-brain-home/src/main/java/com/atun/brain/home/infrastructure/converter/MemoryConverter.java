package com.atun.brain.home.infrastructure.converter;

import com.atun.brain.home.application.service.dto.MemoryResponse;
import com.atun.brain.home.domain.model.entity.Memory;
import com.atun.brain.home.domain.model.vo.MemoryType;
import com.atun.brain.home.infrastructure.persistence.po.MemoryPO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 记忆转换器
 *
 * @author atun-brain
 * @since 1.0
 */
@Component
public class MemoryConverter {

    /**
     * 将领域实体转换为 PO
     */
    public MemoryPO toPO(Memory memory) {
        if (memory == null) {
            return null;
        }
        return MemoryPO.builder()
                .id(memory.getId())
                .userId(memory.getUserId())
                .type(memory.getType() != null ? memory.getType().name() : null)
                .title(memory.getTitle())
                .content(memory.getContent())
                .summary(memory.getSummary())
                .sessionId(memory.getSessionId())
                .metadata(memory.getMetadata())
                .vectorId(memory.getVectorId())
                .isImportant(memory.getIsImportant() != null && memory.getIsImportant() ? 1 : 0)
                .createdAt(memory.getCreatedAt())
                .updatedAt(memory.getUpdatedAt() != null ? memory.getUpdatedAt() : LocalDateTime.now())
                .deleted(memory.getDeleted() != null && memory.getDeleted() ? 1 : 0)
                .build();
    }

    /**
     * 将 PO 转换为领域实体
     */
    public Memory toEntity(MemoryPO po) {
        if (po == null) {
            return null;
        }
        return Memory.builder()
                .id(po.getId())
                .userId(po.getUserId())
                .type(parseMemoryType(po.getType()))
                .title(po.getTitle())
                .content(po.getContent())
                .summary(po.getSummary())
                .sessionId(po.getSessionId())
                .metadata(po.getMetadata())
                .vectorId(po.getVectorId())
                .isImportant(po.getIsImportant() != null && po.getIsImportant() == 1)
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .deleted(po.getDeleted() != null && po.getDeleted() == 1)
                .build();
    }

    /**
     * 将领域实体转换为响应 DTO
     */
    public MemoryResponse toResponse(Memory memory) {
        if (memory == null) {
            return null;
        }
        return MemoryResponse.builder()
                .id(memory.getId())
                .userId(memory.getUserId())
                .type(memory.getType() != null ? memory.getType().name() : null)
                .title(memory.getTitle())
                .content(memory.getContent())
                .summary(memory.getSummary())
                .sessionId(memory.getSessionId())
                .metadata(memory.getMetadata())
                .isImportant(memory.getIsImportant())
                .createdAt(memory.getCreatedAt())
                .build();
    }

    private MemoryType parseMemoryType(String type) {
        if (type == null) {
            return null;
        }
        try {
            return MemoryType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
