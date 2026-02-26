package com.atun.brain.home.domain.repository;

import com.atun.brain.home.domain.model.entity.Category;
import com.atun.brain.home.domain.model.vo.TransactionType;

import java.util.List;
import java.util.Optional;

/**
 * 分类仓储接口
 *
 * @author atun-brain
 * @since 1.0
 */
public interface CategoryRepository {

    /**
     * 保存分类
     *
     * @param category 分类实体
     * @return 保存后的实体
     */
    Category save(Category category);

    /**
     * 根据 ID 查找分类
     *
     * @param id 分类 ID
     * @return 分类实体
     */
    Optional<Category> findById(Long id);

    /**
     * 根据用户 ID 和类型查找分类列表
     *
     * @param userId 用户 ID
     * @param type 交易类型
     * @return 分类列表
     */
    List<Category> findByUserIdAndType(Long userId, TransactionType type);

    /**
     * 根据用户 ID 查找所有分类
     *
     * @param userId 用户 ID
     * @return 分类列表
     */
    List<Category> findByUserId(Long userId);

    /**
     * 删除分类（逻辑删除）
     *
     * @param id 分类 ID
     */
    void delete(Long id);
}
