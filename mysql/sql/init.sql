-- 创建数据库并设置字符集以支持中文
CREATE DATABASE IF NOT EXISTS `atun-brain` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `atun-brain`;
SET NAMES utf8mb4;

-- 用户表
CREATE TABLE user (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      username VARCHAR(64) NOT NULL UNIQUE,
                      email VARCHAR(128) NOT NULL UNIQUE,
                      password_hash VARCHAR(128) NOT NULL,
                      preferences JSON COMMENT '用户偏好设置',
                      active TINYINT(1) DEFAULT 1 COMMENT '是否激活',
                      email_verified TINYINT(1) DEFAULT 0 COMMENT '是否验证邮箱',
                      last_login_at DATETIME COMMENT '最后登录时间',
                      role VARCHAR(32) DEFAULT 'USER' COMMENT '用户角色：ADMIN/USER',
                      avatar_url VARCHAR(255) COMMENT '头像 URL',
                      bio VARCHAR(500) COMMENT '用户简介',
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      INDEX idx_email (email),
                      INDEX idx_username (username),
                      INDEX idx_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 会话表
CREATE TABLE conversation (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              user_id BIGINT NOT NULL,
                              session_id VARCHAR(128) NOT NULL UNIQUE,
                              agent_type VARCHAR(64),
                              last_active_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              archived TINYINT(1) DEFAULT 0 COMMENT '是否归档',
                              title VARCHAR(255) COMMENT '会话标题',
                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              INDEX idx_user_id (user_id),
                              INDEX idx_agent_type (agent_type),
                              INDEX idx_last_active (last_active_at),
                              INDEX idx_user_agent (user_id, agent_type),
                              INDEX idx_archived (archived)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会话表';

-- 消息表
CREATE TABLE message (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         conversation_id BIGINT NOT NULL,
                         role VARCHAR(32) NOT NULL COMMENT 'user/assistant/system',
                         content TEXT NOT NULL,
                         metadata JSON,
                         vector_id VARCHAR(128),
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         INDEX idx_conversation_id (conversation_id),
                         INDEX idx_vector_id (vector_id),
                         INDEX idx_created_at (created_at),
                         INDEX idx_conversation_created (conversation_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- 分类表
CREATE TABLE category (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(64) NOT NULL,
                          type VARCHAR(32) COMMENT '支出/收入',
                          parent_id BIGINT,
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          UNIQUE KEY uq_category_name (name),
                          INDEX idx_type (type),
                          INDEX idx_parent_id (parent_id),
                          INDEX idx_type_parent (type, parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- 交易表
CREATE TABLE transaction (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             user_id BIGINT NOT NULL,
                             message_id BIGINT,
                             amount DECIMAL(12,2) NOT NULL,
                             type VARCHAR(16) NOT NULL COMMENT '支出/收入',
                             category_id BIGINT,
                             transaction_date DATE NOT NULL,
                             description VARCHAR(255),
                             tags JSON,
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             INDEX idx_user_id (user_id),
                             INDEX idx_message_id (message_id),
                             INDEX idx_category_id (category_id),
                             INDEX idx_transaction_date (transaction_date),
                             INDEX idx_type (type),
                             INDEX idx_user_date (user_id, transaction_date),
                             INDEX idx_user_category_date (user_id, category_id, transaction_date),
                             INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易表';

-- 预算表
CREATE TABLE budget (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        user_id BIGINT NOT NULL,
                        category_id BIGINT NOT NULL,
                        amount DECIMAL(12,2) NOT NULL,
                        period_type VARCHAR(16) NOT NULL COMMENT 'MONTHLY/WEEKLY',
                        year INT,
                        month INT,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        INDEX idx_user_id (user_id),
                        INDEX idx_category_id (category_id),
                        INDEX idx_period_type (period_type),
                        INDEX idx_user_category_period (user_id, category_id, year, month),
                        UNIQUE KEY uq_user_category_period (user_id, category_id, period_type, year, month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预算表';

-- 报告表
CREATE TABLE generated_report (
                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  user_id BIGINT NOT NULL,
                                  period_start DATE NOT NULL,
                                  period_end DATE NOT NULL,
                                  report_type VARCHAR(32) NOT NULL COMMENT 'WEEKLY/MONTHLY',
                                  total_expense DECIMAL(12,2) DEFAULT 0.00,
                                  total_income DECIMAL(12,2) DEFAULT 0.00,
                                  net_amount DECIMAL(12,2) DEFAULT 0.00 COMMENT '净额：收入-支出',
                                  category_statistics JSON COMMENT '分类统计（分类ID->金额）',
                                  analysis_text TEXT COMMENT 'AI生成的分析文本',
                                  vector_id VARCHAR(128) COMMENT '向量ID用于语义检索',
                                  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  INDEX idx_user_id (user_id),
                                  INDEX idx_period_start (period_start),
                                  INDEX idx_period_end (period_end),
                                  INDEX idx_report_type (report_type),
                                  INDEX idx_user_period (user_id, period_start, period_end),
                                  INDEX idx_vector_id (vector_id),
                                  INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报告表';

-- ====================================
-- 初始数据插入
-- ====================================

-- 插入默认测试用户
-- 密码: password123 (实际使用时应该使用BCrypt等加密后的hash)
INSERT INTO user (username, email, password_hash, preferences, active, email_verified, role, created_at, updated_at) 
VALUES 
('admin', 'admin@atunbrain.com', '$2a$10$xQKn7P8z3hRqNQz8qGqLz.yF3hH4wR3xWqZzQ3pL5mN7zR8xK9qL2', 
 '{"currency":"CNY","language":"zh-CN","timezone":"Asia/Shanghai"}', 1, 1, 'ADMIN', NOW(), NOW()),
('testuser', 'test@atunbrain.com', '$2a$10$xQKn7P8z3hRqNQz8qGqLz.yF3hH4wR3xWqZzQ3pL5mN7zR8xK9qL2',
 '{"currency":"CNY","language":"zh-CN","timezone":"Asia/Shanghai"}', 1, 1, 'USER', NOW(), NOW());

-- 插入默认支出分类（顶级分类）
INSERT INTO category (name, type, parent_id, created_at, updated_at) VALUES
-- 支出类别
('餐饮', 'EXPENSE', NULL, NOW(), NOW()),
('交通', 'EXPENSE', NULL, NOW(), NOW()),
('购物', 'EXPENSE', NULL, NOW(), NOW()),
('居住', 'EXPENSE', NULL, NOW(), NOW()),
('医疗', 'EXPENSE', NULL, NOW(), NOW()),
('娱乐', 'EXPENSE', NULL, NOW(), NOW()),
('教育', 'EXPENSE', NULL, NOW(), NOW()),
('通讯', 'EXPENSE', NULL, NOW(), NOW()),
('其他支出', 'EXPENSE', NULL, NOW(), NOW()),

-- 收入类别
('工资', 'INCOME', NULL, NOW(), NOW()),
('奖金', 'INCOME', NULL, NOW(), NOW()),
('投资收益', 'INCOME', NULL, NOW(), NOW()),
('兼职', 'INCOME', NULL, NOW(), NOW()),
('红包', 'INCOME', NULL, NOW(), NOW()),
('其他收入', 'INCOME', NULL, NOW(), NOW());

-- 插入支出子分类
-- 餐饮子类（parent_id = 1）
INSERT INTO category (name, type, parent_id, created_at, updated_at) VALUES
('早餐', 'EXPENSE', 1, NOW(), NOW()),
('午餐', 'EXPENSE', 1, NOW(), NOW()),
('晚餐', 'EXPENSE', 1, NOW(), NOW()),
('夜宵', 'EXPENSE', 1, NOW(), NOW()),
('饮料', 'EXPENSE', 1, NOW(), NOW()),
('水果', 'EXPENSE', 1, NOW(), NOW());

-- 交通子类（parent_id = 2）
INSERT INTO category (name, type, parent_id, created_at, updated_at) VALUES
('公交', 'EXPENSE', 2, NOW(), NOW()),
('地铁', 'EXPENSE', 2, NOW(), NOW()),
('出租车', 'EXPENSE', 2, NOW(), NOW()),
('网约车', 'EXPENSE', 2, NOW(), NOW()),
('火车', 'EXPENSE', 2, NOW(), NOW()),
('飞机', 'EXPENSE', 2, NOW(), NOW()),
('加油', 'EXPENSE', 2, NOW(), NOW()),
('停车费', 'EXPENSE', 2, NOW(), NOW());

-- 购物子类（parent_id = 3）
INSERT INTO category (name, type, parent_id, created_at, updated_at) VALUES
('服装', 'EXPENSE', 3, NOW(), NOW()),
('鞋包', 'EXPENSE', 3, NOW(), NOW()),
('化妆品', 'EXPENSE', 3, NOW(), NOW()),
('日用品', 'EXPENSE', 3, NOW(), NOW()),
('电子产品', 'EXPENSE', 3, NOW(), NOW()),
('图书', 'EXPENSE', 3, NOW(), NOW());

-- 居住子类（parent_id = 4）
INSERT INTO category (name, type, parent_id, created_at, updated_at) VALUES
('房租', 'EXPENSE', 4, NOW(), NOW()),
('房贷', 'EXPENSE', 4, NOW(), NOW()),
('水电费', 'EXPENSE', 4, NOW(), NOW()),
('物业费', 'EXPENSE', 4, NOW(), NOW()),
('家具', 'EXPENSE', 4, NOW(), NOW()),
('家电', 'EXPENSE', 4, NOW(), NOW());

-- 医疗子类（parent_id = 5）
INSERT INTO category (name, type, parent_id, created_at, updated_at) VALUES
('挂号费', 'EXPENSE', 5, NOW(), NOW()),
('药品', 'EXPENSE', 5, NOW(), NOW()),
('体检', 'EXPENSE', 5, NOW(), NOW()),
('医疗保健', 'EXPENSE', 5, NOW(), NOW());

-- 娱乐子类（parent_id = 6）
INSERT INTO category (name, type, parent_id, created_at, updated_at) VALUES
('电影', 'EXPENSE', 6, NOW(), NOW()),
('游戏', 'EXPENSE', 6, NOW(), NOW()),
('旅游', 'EXPENSE', 6, NOW(), NOW()),
('健身', 'EXPENSE', 6, NOW(), NOW()),
('会员订阅', 'EXPENSE', 6, NOW(), NOW());

-- 教育子类（parent_id = 7）
INSERT INTO category (name, type, parent_id, created_at, updated_at) VALUES
('学费', 'EXPENSE', 7, NOW(), NOW()),
('培训', 'EXPENSE', 7, NOW(), NOW()),
('在线课程', 'EXPENSE', 7, NOW(), NOW()),
('教材', 'EXPENSE', 7, NOW(), NOW());

-- 通讯子类（parent_id = 8）
INSERT INTO category (name, type, parent_id, created_at, updated_at) VALUES
('话费', 'EXPENSE', 8, NOW(), NOW()),
('宽带', 'EXPENSE', 8, NOW(), NOW()),
('流量', 'EXPENSE', 8, NOW(), NOW());

-- 收入子类
-- 投资收益子类（parent_id = 12）
INSERT INTO category (name, type, parent_id, created_at, updated_at) VALUES
('股票', 'INCOME', 12, NOW(), NOW()),
('基金', 'INCOME', 12, NOW(), NOW()),
('理财', 'INCOME', 12, NOW(), NOW()),
('利息', 'INCOME', 12, NOW(), NOW());

-- ====================================
-- 示例交易数据（用于测试用户 testuser，user_id=2）
-- ====================================

-- 2026年1月的示例交易
INSERT INTO transaction (user_id, amount, type, category_id, transaction_date, description, tags, created_at, updated_at) VALUES
-- 收入
(2, 15000.00, 'INCOME', 10, '2026-01-05', '1月工资', '["工资","月度"]', NOW(), NOW()),
(2, 3000.00, 'INCOME', 11, '2026-01-15', '年终奖金', '["奖金","年终"]', NOW(), NOW()),

-- 支出 - 餐饮
(2, 15.00, 'EXPENSE', 16, '2026-01-02', '早餐包子豆浆', '["早餐"]', NOW(), NOW()),
(2, 35.00, 'EXPENSE', 17, '2026-01-02', '午餐外卖', '["外卖"]', NOW(), NOW()),
(2, 58.00, 'EXPENSE', 18, '2026-01-02', '晚餐聚餐', '["聚餐"]', NOW(), NOW()),
(2, 12.00, 'EXPENSE', 20, '2026-01-03', '咖啡', '["咖啡","下午茶"]', NOW(), NOW()),
(2, 45.00, 'EXPENSE', 21, '2026-01-04', '水果超市', '["水果","健康"]', NOW(), NOW()),

-- 支出 - 交通
(2, 4.00, 'EXPENSE', 23, '2026-01-02', '地铁', '["通勤"]', NOW(), NOW()),
(2, 4.00, 'EXPENSE', 23, '2026-01-03', '地铁', '["通勤"]', NOW(), NOW()),
(2, 35.00, 'EXPENSE', 25, '2026-01-05', '网约车', '["打车","加班"]', NOW(), NOW()),
(2, 300.00, 'EXPENSE', 28, '2026-01-10', '加油', '["汽车","加油"]', NOW(), NOW()),

-- 支出 - 购物
(2, 599.00, 'EXPENSE', 33, '2026-01-08', '冬季外套', '["服装","冬装"]', NOW(), NOW()),
(2, 89.00, 'EXPENSE', 36, '2026-01-12', '洗发水沐浴露', '["日用品"]', NOW(), NOW()),
(2, 2999.00, 'EXPENSE', 37, '2026-01-15', '蓝牙耳机', '["电子产品","耳机"]', NOW(), NOW()),

-- 支出 - 居住
(2, 3500.00, 'EXPENSE', 39, '2026-01-01', '1月房租', '["房租","月度"]', NOW(), NOW()),
(2, 180.00, 'EXPENSE', 41, '2026-01-20', '水电费', '["水电","账单"]', NOW(), NOW()),

-- 支出 - 娱乐
(2, 50.00, 'EXPENSE', 49, '2026-01-18', '看电影', '["电影","休闲"]', NOW(), NOW()),
(2, 299.00, 'EXPENSE', 53, '2026-01-25', '健身卡月费', '["健身","月卡"]', NOW(), NOW()),

-- 支出 - 通讯
(2, 99.00, 'EXPENSE', 61, '2026-01-01', '1月话费', '["话费","月度"]', NOW(), NOW());

-- 2026年2月的最近交易
INSERT INTO transaction (user_id, amount, type, category_id, transaction_date, description, tags, created_at, updated_at) VALUES
(2, 15.00, 'EXPENSE', 16, '2026-02-01', '早餐', '["早餐"]', NOW(), NOW()),
(2, 38.00, 'EXPENSE', 17, '2026-02-01', '午餐', '["午餐"]', NOW(), NOW()),
(2, 4.00, 'EXPENSE', 23, '2026-02-01', '地铁通勤', '["通勤"]', NOW(), NOW()),
(2, 3500.00, 'EXPENSE', 39, '2026-02-01', '2月房租', '["房租","月度"]', NOW(), NOW()),
(2, 100.00, 'EXPENSE', 21, '2026-02-02', '买菜水果', '["买菜","水果"]', NOW(), NOW());

-- ====================================
-- 示例预算数据（用于测试用户 testuser）
-- ====================================

INSERT INTO budget (user_id, category_id, amount, period_type, year, month, created_at, updated_at) VALUES
-- 2026年2月的月度预算
(2, 1, 1500.00, 'MONTHLY', 2026, 2, NOW(), NOW()),  -- 餐饮预算
(2, 2, 500.00, 'MONTHLY', 2026, 2, NOW(), NOW()),   -- 交通预算
(2, 3, 2000.00, 'MONTHLY', 2026, 2, NOW(), NOW()),  -- 购物预算
(2, 4, 4000.00, 'MONTHLY', 2026, 2, NOW(), NOW()),  -- 居住预算
(2, 6, 500.00, 'MONTHLY', 2026, 2, NOW(), NOW()),   -- 娱乐预算
(2, 8, 200.00, 'MONTHLY', 2026, 2, NOW(), NOW());   -- 通讯预算

-- 周度预算示例
INSERT INTO budget (user_id, category_id, amount, period_type, year, month, created_at, updated_at) VALUES
(2, 1, 350.00, 'WEEKLY', NULL, NULL, NOW(), NOW()),  -- 餐饮周预算
(2, 2, 120.00, 'WEEKLY', NULL, NULL, NOW(), NOW());  -- 交通周预算