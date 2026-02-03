package com.atun.brain.infrastructure.persistence.mybatis.impl;

import com.atun.brain.domain.user.entity.User;
import com.atun.brain.domain.user.repository.UserRepository;
import com.atun.brain.domain.user.valueobject.Email;
import com.atun.brain.infrastructure.persistence.mybatis.converter.UserConverter;
import com.atun.brain.infrastructure.persistence.mybatis.mapper.UserMapper;
import com.atun.brain.infrastructure.persistence.mybatis.po.UserPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户仓储实现
 *
 * @author lij
 * @date 2026/02/03
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    
    private final UserMapper userMapper;
    private final UserConverter converter;
    
    @Override
    @Transactional
    public User save(User user) {
        UserPO po = converter.toPO(user);
        
        if (user.getId() == null) {
            userMapper.insert(po);
            user.setId(po.getId());
        } else {
            userMapper.update(po);
        }
        
        return user;
    }
    
    @Override
    public Optional<User> findById(Long id) {
        UserPO po = userMapper.findById(id);
        return Optional.ofNullable(converter.toDomain(po));
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        UserPO po = userMapper.findByUsername(username);
        return Optional.ofNullable(converter.toDomain(po));
    }
    
    @Override
    public Optional<User> findByEmail(Email email) {
        UserPO po = userMapper.findByEmail(email.getValue());
        return Optional.ofNullable(converter.toDomain(po));
    }
    
    @Override
    public List<User> findAllActive() {
        List<UserPO> pos = userMapper.findAllActive();
        return pos.stream()
                .map(converter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<User> findAll() {
        List<UserPO> pos = userMapper.findAll();
        return pos.stream()
                .map(converter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<User> findByCreatedAfter(LocalDateTime dateTime) {
        List<UserPO> pos = userMapper.findByCreatedAfter(dateTime);
        return pos.stream()
                .map(converter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<User> findByLastLoginAfter(LocalDateTime dateTime) {
        List<UserPO> pos = userMapper.findByLastLoginAfter(dateTime);
        return pos.stream()
                .map(converter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<User> findByEmailVerified(boolean verified) {
        List<UserPO> pos = userMapper.findByEmailVerified(verified);
        return pos.stream()
                .map(converter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<User> findAdmins() {
        List<UserPO> pos = userMapper.findByRole("ADMIN");
        return pos.stream()
                .map(converter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void update(User user) {
        UserPO po = converter.toPO(user);
        userMapper.update(po);
    }
    
    @Override
    @Transactional
    public void deleteById(Long id) {
        userMapper.deleteById(id);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return userMapper.existsByUsername(username) > 0;
    }
    
    @Override
    public boolean existsByEmail(Email email) {
        return userMapper.existsByEmail(email.getValue()) > 0;
    }
    
    @Override
    public long count() {
        return userMapper.count();
    }
    
    @Override
    public long countActive() {
        return userMapper.countActive();
    }
}
