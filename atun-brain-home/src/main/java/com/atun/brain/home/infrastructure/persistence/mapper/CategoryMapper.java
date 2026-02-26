package com.atun.brain.home.infrastructure.persistence.mapper;

import com.atun.brain.home.infrastructure.persistence.po.CategoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类 MyBatis Mapper
 *
 * @author atun-brain
 * @since 1.0
 */
@Mapper
public interface CategoryMapper {

    /**
     * 插入分类
     *
     * @param po 分类 PO
     * @return 影响行数
     */
    int insert(CategoryPO po);

    /**
     * 根据 ID 查询分类
     *
     * @param id 分类 ID
     * @return 分类 PO
     */
    CategoryPO selectById(@Param("id") Long id);

    /**
     * 根据用户 ID 和类型查询分类列表
     *
     * @param userId 用户 ID
     * @param type 类型
     * @return 分类列表
     */
    List<CategoryPO> selectByUserIdAndType(
            @Param("userId") Long userId,
            @Param("type") String type);

    /**
     * 根据用户 ID 查询分类列表
     *
     * @param userId 用户 ID
     * @return 分类列表
     */
    List<CategoryPO> selectByUserId(@Param("userId") Long userId);

    /**
     * 更新分类
     *
     * @param po 分类 PO
     * @return 影响行数
     */
    int update(CategoryPO po);

    /**
     * 逻辑删除分类
     *
     * @param id 分类 ID
     * @return 影响行数
     */
    int deleteLogic(@Param("id") Long id);
}
