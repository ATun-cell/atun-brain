# OpenAI集成说明文档

## 架构设计

### DDD分层依赖关系
```
Presentation Layer (Controller)
    ↓
Application Layer (Service)
    ↓ 领域层交互
Domain Layer (Entity/Repository)
    ↓ converts to
Application Layer (DTO Converter)
    ↓ passes DTO to
Infrastructure Layer (OpenAIAdapter)
    ↓ uses
AI Service (Dashscope/OpenAI)
```

**关键原则**：
- ✅ Infrastructure层只依赖基础设施DTO（`AICategoryData`等）
- ✅ Application层负责Domain ↔ Infrastructure DTO转换
- ❌ Infrastructure层**禁止**直接依赖Domain实体

## 配置说明

### 1. 环境变量配置

在 `application.yml` 或环境变量中配置：

```yaml
ai:
  provider: dashscope  # 或 openai
  
  dashscope:
    api-key: sk-your-dashscope-api-key  # 阿里云百炼API密钥
    models:
      chat: qwen-turbo
      analysis: qwen-plus
      report: qwen-max
      embedding: text-embedding-v2
```

或使用环境变量：
```bash
export AI_PROVIDER=dashscope
export DASHSCOPE_API_KEY=sk-your-api-key
```

### 2. 阿里云百炼（Dashscope）配置

1. 访问 [阿里云百炼平台](https://dashscope.console.aliyun.com/)
2. 创建应用并获取API Key
3. 配置可用模型：
   - `qwen-turbo`: 快速对话（低成本）
   - `qwen-plus`: 均衡能力（推荐）
   - `qwen-max`: 高级能力（报告生成）
   - `text-embedding-v2`: 向量化模型

### 3. 切换到OpenAI

```yaml
ai:
  provider: openai
  
  openai:
    api-key: sk-your-openai-api-key
    models:
      chat: gpt-4o-mini
      analysis: gpt-4o
      report: gpt-4o
      embedding: text-embedding-3-small
```

## 功能说明

### 1. 自然语言交易解析

**端点**: `POST /api/transactions/parse?userId=1`

**示例请求**:
```bash
curl -X POST http://localhost:8080/api/transactions/parse?userId=1 \
  -H "Content-Type: text/plain" \
  -d "今天早上在星巴克花了35元买咖啡"
```

**响应**:
```json
{
  "amount": 35.0,
  "type": "EXPENSE",
  "categoryId": 12,
  "description": "星巴克咖啡",
  "tags": ["咖啡", "星巴克", "早餐"],
  "transactionDate": "2026-02-03"
}
```

### 2. AI分类推荐

**端点**: `GET /api/transactions/suggest-category`

**示例请求**:
```bash
curl "http://localhost:8080/api/transactions/suggest-category?userId=1&description=买咖啡&type=EXPENSE"
```

**响应**:
```json
{
  "categoryId": 12,
  "reason": "咖啡属于餐饮类别下的饮品子分类"
}
```

### 3. 财务分析（待实现）

- 交易模式分析
- 财务报告生成
- 预算建议

## 技术实现

### Infrastructure DTO定义

位于 `infrastructure/ai/dto/`:
- `AICategoryData`: 分类轻量级数据
- `AITransactionData`: 交易数据
- `AITransactionParseResult`: 解析结果
- `AICategorySuggestion`: 分类推荐
- `AIAnalysisRequest`: 分析请求
- `AIReportData`: 报告数据
- `AIBudgetRequest`: 预算请求

### Application Service示例

```java
@Override
public AITransactionParseResult parseTransactionFromNaturalLanguage(
        String userMessage, Long userId) {
    
    // 1. 从Domain层获取数据
    List<Category> domainCategories = categoryRepository.findByUserId(userId);
    
    // 2. Domain → Infrastructure DTO转换
    List<AICategoryData> categoryDTOs = domainCategories.stream()
            .map(this::convertToAICategoryData)
            .collect(Collectors.toList());
    
    // 3. 调用Infrastructure层（只使用DTO）
    return openAIAdapter.parseTransaction(userMessage, categoryDTOs);
}
```

### Prompt模板

位于 `infrastructure/ai/prompt/`:
- `TransactionParserPrompt`: 交易解析提示
- `CategoryClassifierPrompt`: 分类推荐提示
- `TransactionAnalysisPrompt`: 交易分析提示
- `ReportGeneratorPrompt`: 报告生成提示
- `BudgetSuggestionPrompt`: 预算建议提示

## 测试步骤

### 1. 启动应用

```bash
# 确保MySQL和Qdrant正在运行
docker-compose up -d

# 启动Spring Boot应用
mvn spring-boot:run
```

### 2. 验证配置

检查启动日志：
```
Creating bean with name 'chatModel'
Creating bean with name 'analysisModel'
Creating bean with name 'reportModel'
Creating bean with name 'embeddingModel'
```

### 3. 测试AI功能

```bash
# 测试交易解析
curl -X POST http://localhost:8080/api/transactions/parse?userId=1 \
  -H "Content-Type: text/plain" \
  -d "今天中午吃饭花了50块"

# 测试分类推荐
curl "http://localhost:8080/api/transactions/suggest-category?userId=1&description=地铁出行&type=EXPENSE"
```

## 错误处理

### 常见问题

1. **API Key未配置**
   - 错误：`ApiKeyNotConfigured`
   - 解决：检查 `application.yml` 或环境变量

2. **模型不存在**
   - 错误：`ModelNotFound`
   - 解决：确认模型名称正确（如 `qwen-turbo`）

3. **超时**
   - 错误：`Timeout`
   - 解决：增加 `ai.dashscope.timeout` 值

4. **重试失败**
   - 错误：`MaxRetriesExceeded`
   - 解决：检查网络连接和AI服务状态

## 性能优化

1. **缓存策略**（待实现）
   - 常用分类列表缓存
   - 交易解析结果缓存

2. **批量处理**（待实现）
   - 批量交易分类
   - 批量向量化

3. **异步处理**（待实现）
   - 报告生成异步化
   - 分析任务队列

## 下一步计划

- [ ] 完善Application Service实现
- [ ] 添加对话历史管理
- [ ] 实现财务分析功能
- [ ] 实现报告生成功能
- [ ] 实现预算建议功能
- [ ] 添加单元测试
- [ ] 添加集成测试
- [ ] 性能优化和缓存
