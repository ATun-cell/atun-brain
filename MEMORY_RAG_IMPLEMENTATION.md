# Memory 和 RAG 模块实现说明

## 概述

本项目基于 `atun-brain-component` 框架的 SPI API 实现了 Memory（记忆）和 RAG（检索增强生成）模块。

---

## Memory 模块实现

### SPI 接口实现

1. **ChatMemoryStore** - `JdbcChatMemoryStore.java`
   - 基于 MyBatis 实现会话记忆的持久化
   - 将对话消息 JSON 序列化存储到 MySQL
   - 支持消息的增删改查操作

2. **ChatMemoryProvider** - `SpringChatMemoryProvider.java`
   - 基于 MessageWindowChatMemory 实现
   - 提供记忆缓存机制
   - 实现记忆清除功能

### 数据库设计

**t_chat_memory 表**
```sql
CREATE TABLE t_chat_memory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    memory_id VARCHAR(255) NOT NULL UNIQUE,  -- userId:sessionId
    messages_json LONGTEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_memory_id (memory_id)
);
```

### 包结构

```
com.atun.brain.home.memory/
├── spi/
│   ├── JdbcChatMemoryStore.java       # ChatMemoryStore 实现
│   └── SpringChatMemoryProvider.java  # ChatMemoryProvider 实现
└── config/
    └── MemorySpiConfig.java           # Spring 配置类
```

---

## RAG 模块实现

### SPI 接口实现

1. **EmbeddingStoreProvider** - `QdrantEmbeddingStoreProvider.java`
   - 基于 Qdrant 向量数据库实现
   - 提供向量存储、检索、删除功能
   - 内部包含 LangChain4j EmbeddingStore 适配器

2. **RetrievalService** - `QdrantRetrievalService.java`
   - 封装向量化 + 搜索的完整 RAG 流程
   - 支持知识入库和检索

### 配置参数

```yaml
agent:
  rag:
    qdrant:
      host: localhost
      port: 6334
      collection: atun-brain
    embedding:
      dimension: 1536
```

### 包结构

```
com.atun.brain.home.rag/
├── spi/
│   ├── QdrantEmbeddingStoreProvider.java  # EmbeddingStoreProvider 实现
│   └── QdrantRetrievalService.java        # RetrievalService 实现
└── config/
    └── RagSpiConfig.java                  # Spring 配置类
```

---

## Spring 配置

### MemorySpiConfig

```java
@Configuration
public class MemorySpiConfig {
    @Bean
    public MemoryWindowConfig memoryWindowConfig() {
        return MemoryWindowConfig.messageWindow(100);
    }
}
```

### RagSpiConfig

```java
@Configuration
public class RagSpiConfig {
    @Bean
    public QdrantClient qdrantClient(...) { ... }

    @Bean
    public QdrantEmbeddingStoreProvider embeddingStoreProvider(...) { ... }

    @Bean
    public QdrantRetrievalService retrievalService(...) { ... }
}
```

---

## Maven 依赖

```xml
<!-- Agent Memory SPI -->
<dependency>
    <groupId>com.atun.brain</groupId>
    <artifactId>agent-memory-spi</artifactId>
</dependency>

<!-- Agent RAG SPI -->
<dependency>
    <groupId>com.atun.brain</groupId>
    <artifactId>agent-rag-spi</artifactId>
</dependency>

<!-- Qdrant Client -->
<dependency>
    <groupId>io.qdrant</groupId>
    <artifactId>client</artifactId>
</dependency>
```

---

## 使用示例

### Memory 使用

通过框架的 `ChatMemoryProvider` SPI 自动注入：

```java
@Autowired
private ChatMemoryProvider chatMemoryProvider;

// 获取会话记忆
ChatMemory memory = chatMemoryProvider.getMemory("user:1:session:abc");
```

### RAG 使用

通过框架的 `RetrievalService` SPI 自动注入：

```java
@Autowired
private RetrievalService retrievalService;

// 检索知识
var result = retrievalService.retrieve("查询内容", 10, Map.of("userId", 1L));

// 入库知识
String id = retrievalService.ingest("知识内容", Map.of("userId", 1L));
```

---

## 环境要求

- Java 21+
- MySQL 8.0+
- Qdrant 1.7+

---

## 编译运行

```bash
# 构建
mvn clean install -Drevision=1.0-SNAPSHOT -DskipTests

# 运行（dev profile）
mvn spring-boot:run -pl atun-brain-home -Dspring-boot.run.profiles=dev
```
