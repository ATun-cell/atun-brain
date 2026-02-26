package com.atun.brain.home.infrastructure.persistence.mapper;

import com.atun.brain.home.infrastructure.persistence.po.MemoryCategoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 记忆分类 MyBatis Mapper
 *
 * @author atun-brain
 * @since 1.0
 */
@Mapper
public interface MemoryCategoryMapper {

    /**
     * 插入记忆分类
     *
     * @param po 记忆分类 PO
     * @return 影响行数
     */
    int insert(MemoryCategoryPO po);

    /**
     * 根据 ID 查询记忆分类
     *
     * @param id 分类 ID
     * @return 记忆分类 PO
     */
    MemoryCategoryPO selectById(@Param("id") Long id);

    /**
     * 根据用户 ID 查询分类列表
     *
     * @param userId 用户 ID
     * @return 分类列表
     */
    List<MemoryCategoryPO> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据父分类 ID 查询子分类
     *
     * @param parentId 父分类 ID
     * @return 分类列表
     */
    List<MemoryCategoryPO> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 更新记忆分类
     *
     * @param po 记忆分类 PO
     * @return 影响行数
     */
    int update(MemoryCategoryPO po);

    /**
     * 逻辑删除记忆分类
     *
     * @param id 分类 ID
     * @return 影响行数
     */
    int deleteLogic(@Param("id") Long id);
}
