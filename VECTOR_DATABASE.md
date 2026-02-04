# 向量数据库集成文档

## 概述

已成功集成 Qdrant 向量数据库，实现基于语义的交易搜索功能。用户可以通过自然语言查询历史交易记录。

## 功能特性

### 1. **向量化存储**
- 自动将交易描述转换为向量（使用阿里云百炼 text-embedding-v2 模型）
- 向量维度：1536
- 存储元数据：transactionId、userId、description、timestamp

### 2. **语义搜索**
- 支持自然语言查询
- 基于余弦相似度搜索
- 支持用户级别的数据隔离

### 3. **自动初始化**
- 应用启动时自动创建向量集合
- 无需手动配置

## 配置说明

### application-dev.yml
```yaml
qdrant:
  host: 101.35.119.204
  port: 6334
  collection-name: transactions
  vector-size: 1536
  use-tls: false
```

## API 使用示例

### 1. 存储交易向量（测试）

**端点**: `POST /api/vector-search/test-store`

```bash
curl -X POST "http://localhost:8089/api/vector-search/test-store?transactionId=1&description=在星巴克买了一杯咖啡&userId=1"
```

**返回**:
```json
"a1b2c3d4-e5f6-7890-abcd-ef1234567890"
```

### 2. 搜索相似交易

**端点**: `GET /api/vector-search/transactions`

```bash
# 搜索关于"咖啡"的交易
curl "http://localhost:8089/api/vector-search/transactions?userId=1&query=买咖啡&limit=5"

# 搜索餐饮相关交易
curl "http://localhost:8089/api/vector-search/transactions?userId=1&query=午餐吃饭&limit=10"

# 搜索交通相关交易
curl "http://localhost:8089/api/vector-search/transactions?userId=1&query=坐地铁出行&limit=5"
```

**返回示例**:
```json
[
  {
    "transactionId": 1,
    "description": "在星巴克买了一杯咖啡",
    "similarity": 0.95
  },
  {
    "transactionId": 15,
    "description": "早餐买了咖啡和三明治",
    "similarity": 0.87
  },
  {
    "transactionId": 23,
    "description": "便利店买咖啡",
    "similarity": 0.82
  }
]
```

## 架构设计

### 分层架构
```
Presentation Layer (VectorSearchController)
    ↓
Application Layer (VectorSearchService)
    ↓ 协调
Infrastructure Layer
    ├── OpenAIAdapter (向量化)
    └── QdrantVectorStoreAdapter (向量存储)
```

### 核心组件

#### 1. **QdrantConfig**
- 配置 Qdrant 客户端连接
- 管理集合属性（名称、维度等）

#### 2. **VectorStoreAdapter**
- 提供向量存储抽象接口
- 支持：存储、搜索、删除、批量操作

#### 3. **QdrantVectorStoreAdapter**
- Qdrant 客户端的具体实现
- 处理向量与元数据的序列化

#### 4. **VectorSearchService**
- 应用层服务
- 整合 AI 向量化和向量存储
- 提供业务级别的搜索功能

## 使用场景

### 场景 1：智能记账助手
用户输入："我上次在哪里买咖啡了？"

系统流程：
1. 将查询文本向量化
2. 在用户的历史交易中搜索相似向量
3. 返回最相关的交易记录

### 场景 2：消费模式分析
用户输入："查找我所有的餐饮支出"

系统流程：
1. 向量化"餐饮支出"查询
2. 返回相似度高的交易（如"午餐"、"晚餐"、"咖啡"等）

### 场景 3：重复交易检测
新交易："星巴克买咖啡 35 元"

系统流程：
1. 向量化新交易描述
2. 搜索历史相似交易
3. 提示用户是否为重复记录

## 性能优化

### 1. 向量维度
- 当前：1536（阿里云 text-embedding-v2）
- 可降维：768 或 384（提高搜索速度）

### 2. 索引优化
- Qdrant 默认使用 HNSW 索引
- 自动优化搜索性能

### 3. 批量操作
- 支持批量存储向量
- 减少网络往返次数

## 集成到现有功能

### 自动向量化交易

在 `TransactionApplicationService` 中集成：

```java
@Override
public TransactionDTO createTransaction(CreateTransactionRequest request, Long userId) {
    // 1. 创建交易（现有逻辑）
    Transaction transaction = null;
    transactionRepository.save(transaction);
    
    // 2. 自动向量化存储
    String vectorId = vectorSearchService.storeTransactionVector(
        transaction.getId(),
        transaction.getDescription(),
        userId
    );
    
    // 3. 更新 vectorId（可选）
    transaction.setVectorId(vectorId);
    transactionRepository.update(transaction);
    
    return convertToDTO(transaction);
}
```

### 智能分类建议

结合向量搜索和 AI 分类：

```java
// 搜索相似历史交易
List<SimilarTransaction> similar = vectorSearchService
    .searchSimilarTransactions(description, userId, 5);

// 分析历史交易的分类模式
// 结合 AI 给出更准确的分类建议
```

## 故障排查

### 问题 1：连接 Qdrant 失败
```
Error: Failed to connect to Qdrant
```

**解决方案**:
1. 检查 Qdrant 服务是否运行：`docker ps | grep qdrant`
2. 验证配置：`qdrant.host` 和 `qdrant.port`
3. 检查防火墙设置

### 问题 2：向量维度不匹配
```
Error: Vector size mismatch
```

**解决方案**:
1. 确认 `qdrant.vector-size` 与 embedding 模型维度一致
2. 删除旧集合：`curl -X DELETE http://host:6333/collections/{name}`
3. 重启应用重新初始化

### 问题 3：搜索结果为空
```
返回：[]
```

**解决方案**:
1. 确认已存储向量数据
2. 检查 userId 过滤条件
3. 调整相似度阈值（当前无阈值限制）

## 下一步计划

- [ ] 集成到交易创建流程（自动向量化）
- [ ] 实现智能去重功能
- [ ] 添加相似交易推荐
- [ ] 支持多维度过滤（时间范围、金额区间）
- [ ] 实现向量索引优化
- [ ] 添加向量搜索性能监控

## 测试建议

### 1. 基础功能测试
```bash
# 存储测试数据
curl -X POST "http://localhost:8089/api/vector-search/test-store?transactionId=1&description=星巴克买咖啡&userId=1"
curl -X POST "http://localhost:8089/api/vector-search/test-store?transactionId=2&description=午餐吃火锅&userId=1"
curl -X POST "http://localhost:8089/api/vector-search/test-store?transactionId=3&description=地铁出行&userId=1"

# 搜索测试
curl "http://localhost:8089/api/vector-search/transactions?userId=1&query=买咖啡&limit=5"
```

### 2. 语义理解测试
```bash
# 测试同义词识别
curl "http://localhost:8089/api/vector-search/transactions?userId=1&query=喝咖啡&limit=5"

# 测试相关性
curl "http://localhost:8089/api/vector-search/transactions?userId=1&query=早餐饮料&limit=5"
```

### 3. 数据隔离测试
```bash
# 用户 1 的交易
curl -X POST "http://localhost:8089/api/vector-search/test-store?transactionId=100&description=买书&userId=1"

# 用户 2 的交易
curl -X POST "http://localhost:8089/api/vector-search/test-store?transactionId=200&description=买书&userId=2"

# 搜索用户 1 的数据（应该只返回 transactionId=100）
curl "http://localhost:8089/api/vector-search/transactions?userId=1&query=买书&limit=10"
```

## 监控指标

建议监控以下指标：
- 向量化耗时
- 搜索响应时间
- 存储成功率
- 集合大小
- 查询 QPS

---

向量数据库集成已完成，可以开始测试和使用！🎉
