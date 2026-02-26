package com.atun.brain.home.infrastructure.persistence.repository;

import com.atun.brain.home.domain.model.entity.Category;
import com.atun.brain.home.domain.model.vo.TransactionType;
import com.atun.brain.home.domain.repository.CategoryRepository;
import com.atun.brain.home.infrastructure.converter.CategoryConverter;
import com.atun.brain.home.infrastructure.persistence.mapper.CategoryMapper;
import com.atun.brain.home.infrastructure.persistence.po.CategoryPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 分类仓储实现
 *
 * @author atun-brain
 * @since 1.0
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryMapper categoryMapper;
    private final CategoryConverter converter;

    @Override
    public Category save(Category category) {
        if (category == null) {
            return null;
        }

        CategoryPO po = converter.toPO(category);

        int result;
        if (category.getId() == null) {
            // 新增
            result = categoryMapper.insert(po);
            log.debug("[CategoryRepository] 新增分类，id={}", po.getId());
        } else {
            // 更新
            result = categoryMapper.update(po);
            log.debug("[CategoryRepository] 更新分类，id={}", category.getId());
        }

        if (result > 0) {
            return converter.toEntity(po);
        }
        return null;
    }

    @Override
    public Optional<Category> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        CategoryPO po = categoryMapper.selectById(id);
        if (po == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(converter.toEntity(po));
    }

    @Override
    public List<Category> findByUserIdAndType(Long userId, TransactionType type) {
        if (userId == null || type == null) {
            return new ArrayList<>();
        }

        List<CategoryPO> poList = categoryMapper.selectByUserIdAndType(userId, type.name());
        if (poList == null || poList.isEmpty()) {
            return new ArrayList<>();
        }

        return poList.stream()
                .map(converter::toEntity)
                .toList();
    }

    @Override
    public List<Category> findByUserId(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }

        List<CategoryPO> poList = categoryMapper.selectByUserId(userId);
        if (poList == null || poList.isEmpty()) {
            return new ArrayList<>();
        }

        return poList.stream()
                .map(converter::toEntity)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            return;
        }

        int result = categoryMapper.deleteLogic(id);
        log.debug("[CategoryRepository] 删除分类，id={}, result={}", id, result);
    }
}
