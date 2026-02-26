package com.atun.brain.home.infrastructure.persistence.mapper;

import com.atun.brain.home.infrastructure.persistence.po.TransactionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 交易 MyBatis Mapper
 *
 * @author atun-brain
 * @since 1.0
 */
@Mapper
public interface TransactionMapper {

    /**
     * 插入交易
     *
     * @param po 交易 PO
     * @return 影响行数
     */
    int insert(TransactionPO po);

    /**
     * 根据 ID 查询交易
     *
     * @param id 交易 ID
     * @return 交易 PO
     */
    TransactionPO selectById(@Param("id") Long id);

    /**
     * 根据用户 ID 查询交易列表
     *
     * @param userId 用户 ID
     * @return 交易列表
     */
    List<TransactionPO> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据时间范围查询交易
     *
     * @param userId 用户 ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 交易列表
     */
    List<TransactionPO> selectByUserIdAndTimeRange(
            @Param("userId") Long userId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 根据分类 ID 查询交易
     *
     * @param userId 用户 ID
     * @param categoryId 分类 ID
     * @return 交易列表
     */
    List<TransactionPO> selectByUserIdAndCategoryId(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId);

    /**
     * 更新交易
     *
     * @param po 交易 PO
     * @return 影响行数
     */
    int update(TransactionPO po);

    /**
     * 逻辑删除交易
     *
     * @param id 交易 ID
     * @return 影响行数
     */
    int deleteLogic(@Param("id") Long id);
}
