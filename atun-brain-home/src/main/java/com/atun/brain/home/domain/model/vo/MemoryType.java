package com.atun.brain.home.domain.model.vo;

/**
 * 记忆类型枚举值对象
 *
 * @author atun-brain
 * @since 1.0
 */
public enum MemoryType {

    /**
     * 对话记忆
     */
    CONVERSATION("对话记忆"),

    /**
     * 事件记忆
     */
    EVENT("事件记忆"),

    /**
     * 知识记忆
     */
    KNOWLEDGE("知识记忆"),

    /**
     * 人物记忆
     */
    PERSON("人物记忆"),

    /**
     * 地点记忆
     */
    PLACE("地点记忆"),

    /**
     * 媒体记忆
     */
    MEDIA("媒体记忆");

    private final String description;

    MemoryType(String description) {
        this.description = description;
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    public String getDescription() {
        return description;
    }
}
