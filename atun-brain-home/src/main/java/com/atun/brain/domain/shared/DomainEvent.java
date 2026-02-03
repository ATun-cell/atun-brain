package com.atun.brain.domain.shared;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 领域事件基类
 * 用于在聚合根内部或聚合根之间传递事件
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
public abstract class DomainEvent {
    
    /** 事件发生时间 */
    private final LocalDateTime occurredOn;
    
    /** 事件类型 */
    private final String eventType;
    
    protected DomainEvent(String eventType) {
        this.occurredOn = LocalDateTime.now();
        this.eventType = eventType;
    }
}
