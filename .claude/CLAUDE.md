# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run
- 项目基于java21，本地的java21版本的地址是C:\dev\tools\jdks\jdk-21不要去查询本地环境变量中的java版本。
```bash
# Build all modules
mvn clean install -Drevision=1.0-SNAPSHOT -DskipTests

# Run application (dev profile)
mvn spring-boot:run -pl atun-brain-home -Dspring-boot.run.profiles=dev

# Run tests
mvn test
```

## Infrastructure Setup

```bash
docker-compose up -d
```

- MySQL: localhost:13307 (database: atun-brain)
- Qdrant: localhost:6334 (gRPC) / localhost:6333 (REST)

## Architecture Overview

**AI Agent 应用** - 通过自然语言对话调用工具，无需 REST Controller

```
atun-brain/
├── atun-brain-home/    # 主应用 - ToolProvider + 业务逻辑
└── agent-tools-impl/   # 额外工具实现
```

### Core Stack

| Layer | Technology |
|-------|------------|
| Framework | Spring Boot 3.2.2 |
| AI/LLM | LangChain4j 1.0.0-beta2 |
| LLM Provider | 阿里云百炼 (qwen) / OpenAI |
| Vector DB | Qdrant |
| RDBMS | MySQL 8.0 + MyBatis |

### Module Responsibilities

**atun-brain-home**
- ToolProvider 实现（Finance, Memory, Decision）
- Application Service（业务编排）
- Domain Service（业务规则）
- MyBatis mappers 和 entities

**agent-tools-impl**
- 额外的 ToolProvider 实现

### Configuration

环境变量：
- `MYSQL_HOST`, `MYSQL_PORT`, `MYSQL_DATABASE`, `MYSQL_USER`, `MYSQL_ROOT_PASSWORD`
- `DASHSCOPE_API_KEY` / `OPENAI_API_KEY`
- `QDRANT_HOST`, `QDRANT_PORT`, `QDRANT_COLLECTION`
- `SERVER_PORT`

### External Dependencies
atun-brain-componen的本机地址C:\dev\workspace\atun-brain-component你可以直接访问
`atun-brain-component` (version 1.0-SNAPSHOT) 提供:
- `agent-tools-spi` - ToolProvider 接口
- `agent-memory-spi` / `agent-memory-jdbc` - 记忆持久化
- `agent-rag-spi` / `agent-rag-qdrant` - RAG 向量检索
- `agent-core` - Agent 编排器 (四阶段 Pipeline)
- `agent-config-spring-boot-starter` - 自动配置

---

## Framework Integration

### atun-brain-component 框架代码映射

框架位置：`C:\dev\workspace\atun-brain-component` / `git@github.com:ATun-cell/atun-brain-component.git`

#### 核心 SPI 接口

| SPI 接口 | 包路径 | 用途 |
|----------|--------|------|
| `ToolProvider` | `com.atun.brain.agent.tools.spi` | 工具提供者接口 |
| `ToolResult` | `com.atun.brain.agent.tools.spi` | 工具执行结果 |
| `ToolRouteDecision` | `com.atun.brain.agent.tools.spi` | 意图分类输出 |
| `ChatMemoryProvider` | `com.atun.brain.agent.memory.spi` | 会话记忆提供者 |
| `RetrievalService` | `com.atun.brain.agent.rag.spi` | RAG 检索服务 |

#### 四阶段 Pipeline

```
1. IntentClassifier    → ToolRouteDecision
2. ToolOrchestrator    → 执行工具或直连 LLM
3. ResponseComposer    → 融合结果 + RAG 增强
4. MemoryPersister     → 持久化记忆
```

### ToolProvider 开发模板

```java
package com.atun.brain.home.tool.finance;

import com.atun.brain.agent.tools.spi.ToolProvider;
import com.atun.brain.agent.tools.spi.ToolResult;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.V;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class FinanceToolProvider implements ToolProvider {

    @Override
    public String getGroupName() { return "finance-tools"; }

    @Override
    public String getDescription() { return "个人记账和财务分析工具集"; }

    @Override
    public List<Object> getToolObjects() {
        return List.of(new FinanceTools());
    }

    @Override
    public int getOrder() { return 100; }

    public class FinanceTools {
        @Tool("记录用户的收支交易")
        public ToolResult recordTransaction(
                @V("userId") Long userId,
                @V("amount") BigDecimal amount,
                @V("type") String type,
                @V("description") String description) {
            // 调用 Application Service
            return ToolResult.success(...);
        }
    }
}
```

### @Tool 方法规范

- **返回类型**: `String` 或 `ToolResult`
- **参数注解**: 使用 `@V("参数名")`
- **方法描述**: @Tool 注解描述功能，供 LLM 理解

### ToolRouteDecision 路由策略

| 策略 | 说明 |
|------|------|
| `DIRECT_LLM` | 直接 LLM 回答 |
| `DIRECT_TOOL` | 调用指定工具 |
| `AUTO_TOOL_CHAIN` | AiServices 自动判断 |

---

## DDD Architecture

### 包结构

```
com.atun.brain.home/
├── application/service/      # 应用服务 (编排层)
│   ├── TransactionAppService.java
│   ├── MemoryAppService.java
│   ├── dto/                  # Response DTOs
│   └── impl/                 # 实现
├── domain/
│   ├── model/entity/         # 聚合根
│   ├── model/vo/             # 值对象
│   ├── service/              # 领域服务
│   └── repository/           # 仓储接口
├── infrastructure/
│   ├── persistence/          # MyBatis + Repository 实现
│   ├── converter/            # Entity/PO/DTO 转换
│   └── config/exception/
└── tool/                     # ToolProvider
    ├── finance/
    ├── memory/
    └── decision/
```

### 各层职责

| 层级 | 职责 |
|------|------|
| application | 应用编排，事务管理 |
| domain | 核心业务逻辑 |
| infrastructure | 持久化、配置、异常处理 |
| tool | ToolProvider 实现，调用应用服务 |

### 命名约定

| 类型 | 规范 | 示例 |
|------|------|------|
| Entity | 名词 | `Transaction`, `Memory` |
| VO | 名词 | `Money`, `TransactionType` |
| Repository | `XxxRepository` | `TransactionRepository` |
| AppService | `XxxAppService` | `TransactionAppService` |
| DomainService | `XxxDomainService` | `TransactionDomainService` |
| ToolProvider | `XxxToolProvider` | `FinanceToolProvider` |
| PO | `XxxPO` | `TransactionPO` |
| Mapper | `XxxMapper` | `TransactionMapper` |
| Converter | `XxxConverter` | `TransactionConverter` |
| Response | `XxxResponse` | `TransactionResponse` |

### 数据库规范

- 表名：`t_{模块}_{实体}` 如 `t_finance_transaction`
- 主键：`id` (BIGINT)
- 通用字段：`created_at`, `updated_at`, `deleted`
- Mapper XML: `classpath:mapper/{模块}/*.xml`

---

## Development Workflow

### Git

```
main          - 主分支
feat-*        - 功能分支
fix-*         - 修复分支
```

### Commit 规范

`{type}: {subject}`

- `feat` - 新功能
- `fix` - Bug 修复
- `refactor` - 重构
- `docs` - 文档

### Commands

```bash
# 构建
mvn clean install -Drevision=1.0-SNAPSHOT -DskipTests

# 运行
mvn spring-boot:run -pl atun-brain-home -Dspring-boot.run.profiles=dev

# 测试单个类
mvn test -Dtest=ClassName#testMethod
```

---

## Project Structure

```
atun-brain-home/
├── application/service/
│   ├── TransactionAppService.java
│   ├── MemoryAppService.java
│   ├── StatisticsResponse.java
│   ├── dto/
│   │   ├── TransactionResponse.java
│   │   └── MemoryResponse.java
│   └── impl/
│       ├── TransactionAppServiceImpl.java
│       └── MemoryAppServiceImpl.java
├── domain/
│   ├── model/
│   │   ├── entity/
│   │   │   ├── Transaction.java
│   │   │   ├── Category.java
│   │   │   ├── Memory.java
│   │   │   └── MemoryCategory.java
│   │   └── vo/
│   │       ├── TransactionType.java
│   │       ├── MemoryType.java
│   │       └── Money.java
│   ├── repository/
│   │   ├── TransactionRepository.java
│   │   ├── CategoryRepository.java
│   │   ├── MemoryRepository.java
│   │   └── MemoryCategoryRepository.java
│   └── service/
│       ├── TransactionDomainService.java
│       └── MemoryDomainService.java
├── infrastructure/
│   ├── converter/
│   │   ├── TransactionConverter.java
│   │   ├── CategoryConverter.java
│   │   └── MemoryConverter.java
│   ├── persistence/
│   │   ├── mapper/
│   │   ├── po/
│   │   └── repository/
│   ├── config/
│   └── exception/
└── tool/
    ├── finance/
    │   └── FinanceToolProvider.java
    └── memory/
        └── MemoryToolProvider.java
```
