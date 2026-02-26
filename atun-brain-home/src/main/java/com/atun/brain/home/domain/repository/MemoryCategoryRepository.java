package com.atun.brain.home.domain.repository;

import com.atun.brain.home.domain.model.entity.MemoryCategory;

import java.util.List;
import java.util.Optional;

/**
 * 记忆分类仓储接口
 *
 * @author atun-brain
 * @since 1.0
 */
public interface MemoryCategoryRepository {

    /**
     * 保存记忆分类
     *
     * @param category 记忆分类实体
     * @return 保存后的实体
     */
    MemoryCategory save(MemoryCategory category);

    /**
     * 根据 ID 查找记忆分类
     *
     * @param id 分类 ID
     * @return 记忆分类实体
     */
    Optional<MemoryCategory> findById(Long id);

    /**
     * 根据用户 ID 查找分类列表
     *
     * @param userId 用户 ID
     * @return 分类列表
     */
    List<MemoryCategory> findByUserId(Long userId);

    /**
     * 根据父分类 ID 查找子分类
     *
     * @param parentId 父分类 ID
     * @return 分类列表
     */
    List<MemoryCategory> findByParentId(Long parentId);

    /**
     * 删除记忆分类（逻辑删除）
     *
     * @param id 分类 ID
     */
    void delete(Long id);
}
