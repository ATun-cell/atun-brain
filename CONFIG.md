# 配置说明

## Spring Boot 配置方式

本项目已切换为标准的 Spring Boot 配置方式，不再使用 `.env` 文件。

### 配置文件结构

```
src/main/resources/
├── application.yml          # 主配置文件（所有环境通用，使用环境变量占位符）
└── application-dev.yml      # 开发环境专用配置（本地开发使用）
```

### 开发环境配置

#### 方式1：使用 application-dev.yml（推荐）

已配置好的开发环境文件 `application-dev.yml` 包含：
- MySQL: `101.35.119.204:13307`
- 阿里云百炼 API Key: `sk-1e878fbcf3b748fba0ca9a9fcf674309`
- Qdrant: `101.35.119.204:6334`

**启动应用**：
```bash
# 方式1: Maven 启动
mvn spring-boot:run

# 方式2: Java 启动（先打包）
mvn clean package
java -jar target/atun-brain-home-1.0.0.jar

# 注：application.yml 已设置 spring.profiles.active=dev，会自动加载 application-dev.yml
```

#### 方式2：使用环境变量（生产环境推荐）

如需覆盖配置，可使用环境变量：

**Windows PowerShell**:
```powershell
$env:DASHSCOPE_API_KEY="your-api-key"
$env:MYSQL_HOST="101.35.119.204"
$env:MYSQL_PORT="13307"
mvn spring-boot:run
```

**Linux/macOS**:
```bash
export DASHSCOPE_API_KEY=your-api-key
export MYSQL_HOST=101.35.119.204
export MYSQL_PORT=13307
mvn spring-boot:run
```

**IDEA 运行配置**:
1. Run → Edit Configurations
2. Environment variables 中添加：
   ```
   DASHSCOPE_API_KEY=your-api-key;MYSQL_HOST=101.35.119.204
   ```

### 生产环境部署

生产环境**不要**将敏感信息写在配置文件中，建议：

1. **使用系统环境变量**
   ```bash
   export DASHSCOPE_API_KEY=sk-prod-key
   export MYSQL_ROOT_PASSWORD=strong-password
   java -jar app.jar
   ```

2. **使用 application-prod.yml**（不提交到Git）
   创建 `application-prod.yml` 并添加到 `.gitignore`

3. **使用外部配置文件**
   ```bash
   java -jar app.jar --spring.config.location=/etc/app/config/
   ```

### 配置优先级（从高到低）

1. 命令行参数: `--server.port=8080`
2. 环境变量: `SERVER_PORT=8080`
3. application-{profile}.yml: `application-dev.yml`
4. application.yml: 默认值

### 检查当前配置

启动应用后查看日志，确认配置是否正确：
```
The following 1 profile is active: "dev"
Datasource URL: jdbc:mysql://101.35.119.204:13307/atun_brain
AI Provider: dashscope
```

### 迁移说明

- ✅ `.env.example` 已重命名为 `.env.example.deprecated`（已弃用）
- ✅ 所有配置已迁移到 `application-dev.yml`
- ✅ Spring Boot 会自动读取配置文件，无需额外依赖

### 常见问题

**Q: 如何切换到 OpenAI？**
```yaml
# 在 application-dev.yml 中修改
ai:
  provider: openai
  openai:
    api-key: sk-your-openai-key
    base-url: https://api.openai.com/v1
```

**Q: 如何添加新的环境（如测试环境）？**
创建 `application-test.yml`，然后启动时指定：
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

**Q: 配置没有生效？**
1. 检查 `application.yml` 中 `spring.profiles.active` 是否为 `dev`
2. 确认文件名为 `application-dev.yml`（不是 `application-dev.yaml`）
3. 查看启动日志中的 "active profile"
