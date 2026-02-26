-- 创建数据库并设置字符集以支持中文
CREATE DATABASE IF NOT EXISTS `atun-brain` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `atun-brain`;
SET NAMES utf8mb4;

-- ====================================
-- 财务模块表结构
-- ====================================

-- 分类表
CREATE TABLE t_finance_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类 ID',
    user_id BIGINT NOT NULL DEFAULT 0 COMMENT '用户 ID（0 表示系统预置分类）',
    type VARCHAR(16) NOT NULL COMMENT '分类类型：INCOME/EXPENSE',
    name VARCHAR(64) NOT NULL COMMENT '分类名称',
    icon VARCHAR(128) COMMENT '分类图标',
    sort_order INT DEFAULT 0 COMMENT '排序值',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否已删除：0-否，1-是',
    UNIQUE KEY uq_user_name (user_id, name),
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_user_type (user_id, type),
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='财务分类表';

-- 交易表
CREATE TABLE t_finance_transaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '交易 ID',
    user_id BIGINT NOT NULL COMMENT '用户 ID',
    type VARCHAR(16) NOT NULL COMMENT '交易类型：INCOME/EXPENSE',
    amount DECIMAL(12,2) NOT NULL COMMENT '交易金额',
    category_id BIGINT COMMENT '分类 ID',
    description VARCHAR(500) COMMENT '交易描述',
    transaction_time DATETIME NOT NULL COMMENT '交易时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否已删除：0-否，1-是',
    INDEX idx_user_id (user_id),
    INDEX idx_category_id (category_id),
    INDEX idx_transaction_time (transaction_time),
    INDEX idx_type (type),
    INDEX idx_user_date (user_id, transaction_time),
    INDEX idx_user_category_date (user_id, category_id, transaction_time),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='财务交易表';

-- ====================================
-- 初始数据 - 系统预置分类（user_id=0）
-- ====================================

-- 支出类别
INSERT INTO t_finance_category (name, type, user_id, icon, sort_order, created_at, updated_at) VALUES
('餐饮', 'EXPENSE', 0, '🍜', 1, NOW(), NOW()),
('交通', 'EXPENSE', 0, '🚗', 2, NOW(), NOW()),
('购物', 'EXPENSE', 0, '🛍️', 3, NOW(), NOW()),
('居住', 'EXPENSE', 0, '🏠', 4, NOW(), NOW()),
('医疗', 'EXPENSE', 0, '🏥', 5, NOW(), NOW()),
('娱乐', 'EXPENSE', 0, '🎮', 6, NOW(), NOW()),
('教育', 'EXPENSE', 0, '📚', 7, NOW(), NOW()),
('通讯', 'EXPENSE', 0, '📱', 8, NOW(), NOW()),
('其他支出', 'EXPENSE', 0, '📝', 9, NOW(), NOW());

-- 收入类别
INSERT INTO t_finance_category (name, type, user_id, icon, sort_order, created_at, updated_at) VALUES
('工资', 'INCOME', 0, '💰', 1, NOW(), NOW()),
('奖金', 'INCOME', 0, '🎁', 2, NOW(), NOW()),
('投资收益', 'INCOME', 0, '📈', 3, NOW(), NOW()),
('兼职', 'INCOME', 0, '💼', 4, NOW(), NOW()),
('红包', 'INCOME', 0, '🧧', 5, NOW(), NOW()),
('其他收入', 'INCOME', 0, '💵', 6, NOW(), NOW());

-- ====================================
-- 示例交易数据（用于测试用户 user_id=1）
-- ====================================

-- 2026 年 1 月的示例交易
INSERT INTO t_finance_transaction (user_id, amount, type, category_id, transaction_time, description, created_at, updated_at) VALUES
-- 收入
(1, 15000.00, 'INCOME', 10, '2026-01-05 09:00:00', '1 月工资', NOW(), NOW()),
(1, 3000.00, 'INCOME', 11, '2026-01-15 09:00:00', '年终奖金', NOW(), NOW()),

-- 支出 - 餐饮
(1, 15.00, 'EXPENSE', 1, '2026-01-02 08:00:00', '早餐包子豆浆', NOW(), NOW()),
(1, 35.00, 'EXPENSE', 1, '2026-01-02 12:00:00', '午餐外卖', NOW(), NOW()),
(1, 58.00, 'EXPENSE', 1, '2026-01-02 19:00:00', '晚餐聚餐', NOW(), NOW()),
(1, 12.00, 'EXPENSE', 1, '2026-01-03 15:00:00', '咖啡', NOW(), NOW()),
(1, 45.00, 'EXPENSE', 1, '2026-01-04 10:00:00', '水果超市', NOW(), NOW()),

-- 支出 - 交通
(1, 4.00, 'EXPENSE', 2, '2026-01-02 08:30:00', '地铁通勤', NOW(), NOW()),
(1, 4.00, 'EXPENSE', 2, '2026-01-03 08:30:00', '地铁通勤', NOW(), NOW()),
(1, 35.00, 'EXPENSE', 2, '2026-01-05 21:00:00', '网约车加班回家', NOW(), NOW()),
(1, 300.00, 'EXPENSE', 2, '2026-01-10 14:00:00', '加油', NOW(), NOW()),

-- 支出 - 购物
(1, 599.00, 'EXPENSE', 3, '2026-01-08 15:00:00', '冬季外套', NOW(), NOW()),
(1, 89.00, 'EXPENSE', 3, '2026-01-12 11:00:00', '洗发水沐浴露', NOW(), NOW()),
(1, 2999.00, 'EXPENSE', 3, '2026-01-15 16:00:00', '蓝牙耳机', NOW(), NOW()),

-- 支出 - 居住
(1, 3500.00, 'EXPENSE', 4, '2026-01-01 10:00:00', '1 月房租', NOW(), NOW()),
(1, 180.00, 'EXPENSE', 4, '2026-01-20 14:00:00', '水电费', NOW(), NOW()),

-- 支出 - 娱乐
(1, 50.00, 'EXPENSE', 6, '2026-01-18 20:00:00', '看电影', NOW(), NOW()),
(1, 299.00, 'EXPENSE', 6, '2026-01-25 10:00:00', '健身卡月费', NOW(), NOW()),

-- 支出 - 通讯
(1, 99.00, 'EXPENSE', 8, '2026-01-01 09:00:00', '1 月话费', NOW(), NOW());

-- 2026 年 2 月的最近交易
INSERT INTO t_finance_transaction (user_id, amount, type, category_id, transaction_time, description, created_at, updated_at) VALUES
(1, 15.00, 'EXPENSE', 1, '2026-02-01 08:00:00', '早餐', NOW(), NOW()),
(1, 38.00, 'EXPENSE', 1, '2026-02-01 12:00:00', '午餐', NOW(), NOW()),
(1, 4.00, 'EXPENSE', 2, '2026-02-01 08:30:00', '地铁通勤', NOW(), NOW()),
(1, 3500.00, 'EXPENSE', 4, '2026-02-01 10:00:00', '2 月房租', NOW(), NOW()),
(1, 100.00, 'EXPENSE', 1, '2026-02-02 17:00:00', '买菜水果', NOW(), NOW());
