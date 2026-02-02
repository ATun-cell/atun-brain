-- 用户表
CREATE TABLE user (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      username VARCHAR(64) NOT NULL UNIQUE,
                      email VARCHAR(128) NOT NULL UNIQUE,
                      password_hash VARCHAR(128) NOT NULL,
                      preferences JSON,
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 会话表
CREATE TABLE conversation (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              user_id BIGINT NOT NULL,
                              session_id VARCHAR(128) NOT NULL UNIQUE,
                              agent_type VARCHAR(64),
                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              last_active_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              INDEX idx_user_id (user_id),
                              INDEX idx_agent_type (agent_type),
                              INDEX idx_last_active (last_active_at),
                              INDEX idx_user_agent (user_id, agent_type)
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
                        period VARCHAR(16) NOT NULL COMMENT 'month/week',
                        year INT,
                        month INT,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        INDEX idx_user_id (user_id),
                        INDEX idx_category_id (category_id),
                        INDEX idx_period (period),
                        INDEX idx_user_category_period (user_id, category_id, year, month),
                        UNIQUE KEY uq_user_category_period (user_id, category_id, period, year, month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预算表';

-- 报告表
CREATE TABLE generated_report (
                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  user_id BIGINT NOT NULL,
                                  period_start DATE NOT NULL,
                                  period_end DATE NOT NULL,
                                  report_data JSON,
                                  vector_id VARCHAR(128),
                                  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  INDEX idx_user_id (user_id),
                                  INDEX idx_period_start (period_start),
                                  INDEX idx_period_end (period_end),
                                  INDEX idx_user_period (user_id, period_start, period_end),
                                  INDEX idx_vector_id (vector_id),
                                  INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报告表';