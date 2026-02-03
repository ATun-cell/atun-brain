package com.atun.brain.domain.shared;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 基础实体 - 所有领域实体的基类
 * 提供通用的ID和时间戳字段
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
public abstract class BaseEntity {
    
    /** 实体唯一标识 */
    protected Long id;
    
    /** 创建时间 */
    protected LocalDateTime createdAt;
    
    /** 更新时间 */
    protected LocalDateTime updatedAt;
    
    /**
     * 判断是否为新实体（未持久化）
     */
    public boolean isNew() {
        return this.id == null;
    }
    
    /**
     * 基于ID的相等性判断
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        if (id == null || that.id == null) {
            return false;
        }
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
