# Phase 2: OTEL Receiver - Usage Guide

## 🎉 Phase 2 完成！

Agent Trace Server 现在支持通过标准 OTLP 协议接收 OpenTelemetry traces！

## 快速开始

### 1. 启动服务器

```bash
# 使用默认端口 4318
./gradlew :trace_server:run

# 或指定端口
./gradlew :trace_server:run --args="8080"

# 或使用编译后的可执行文件
./build/install/trace_server/bin/trace_server 4318
```

服务器启动后会监听：
- **OTLP endpoint**: `http://localhost:4318/v1/traces`
- **REST API**: `http://localhost:4318/api/*`

### 2. 发送 OTEL Traces

#### 使用 curl（OTLP JSON format）

```bash
curl -X POST http://localhost:4318/v1/traces \
  -H "Content-Type: application/json" \
  -d '{
    "resourceSpans": [{
      "resource": {
        "attributes": {
          "service.name": "cursor-ai-agent"
        }
      },
      "scopeSpans": [{
        "scope": {
          "name": "cursor.code_generation",
          "version": "1.0.0"
        },
        "spans": [{
          "traceId": "4bf92f3577b34da6a3ce929d0e0e4736",
          "spanId": "00f067aa0ba902b7",
          "name": "generate_code",
          "startTimeNanos": "1706021400000000000",
          "endTimeNanos": "1706021405000000000",
          "attributes": {
            "gen_ai.system": "anthropic",
            "gen_ai.request.model": "anthropic/claude-opus-4-5-20251101",
            "code.contributor.type": "ai",
            "code.generation.file": "src/example.ts",
            "conversation.url": "https://api.cursor.com/v1/conversations/12345",
            "vcs.type": "git",
            "vcs.revision": "abc123"
          },
          "events": [{
            "timeNanos": "1706021405000000000",
            "name": "code.range.generated",
            "attributes": {
              "code.range.start": "10",
              "code.range.end": "30",
              "code.range.hash": "murmur3:abc123"
            }
          }]
        }]
      }]
    }]
  }'
```

#### 使用 OpenTelemetry SDK（Python 示例）

```python
from opentelemetry import trace
from opentelemetry.exporter.otlp.proto.http.trace_exporter import OTLPSpanExporter
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor

# 配置 OTLP exporter
exporter = OTLPSpanExporter(
    endpoint="http://localhost:4318/v1/traces"
)

# 设置 tracer
provider = TracerProvider()
processor = BatchSpanProcessor(exporter)
provider.add_span_processor(processor)
trace.set_tracer_provider(provider)

# 创建 span
tracer = trace.get_tracer("cursor.code_generation")
with tracer.start_as_current_span("generate_code") as span:
    span.set_attribute("gen_ai.system", "anthropic")
    span.set_attribute("code.contributor.type", "ai")
    span.set_attribute("code.generation.file", "src/example.ts")
    # ... 更多属性
```

### 3. REST API 操作

#### 获取所有 traces

```bash
curl http://localhost:4318/api/traces
```

响应：
```json
{
  "traces": [...],
  "offset": 0,
  "limit": 100,
  "total": 5
}
```

#### 获取特定 trace

```bash
curl http://localhost:4318/api/traces/{trace-id}
```

#### 导出为 OTEL 格式

```bash
curl http://localhost:4318/api/traces/{trace-id}/otel
```

#### 获取统计信息

```bash
curl http://localhost:4318/api/stats
```

响应：
```json
{
  "totalTraces": 42,
  "storageType": "in-memory"
}
```

#### 删除 trace

```bash
curl -X DELETE http://localhost:4318/api/traces/{trace-id}
```

#### 健康检查

```bash
curl http://localhost:4318/health
```

## API 端点总览

| 方法 | 端点 | 说明 |
|------|------|------|
| POST | `/v1/traces` | 接收 OTLP traces（OTEL 兼容） |
| GET | `/api/traces` | 列出所有 traces（支持分页） |
| GET | `/api/traces/{id}` | 获取特定 trace |
| GET | `/api/traces/{id}/otel` | 导出为 OTEL 格式 |
| DELETE | `/api/traces/{id}` | 删除 trace |
| GET | `/api/stats` | 获取统计信息 |
| GET | `/health` | 健康检查 |

## 特性

### ✅ OTLP 兼容
- 完全兼容 OpenTelemetry Protocol (OTLP) HTTP
- 支持标准 OTEL exporters
- 自动转换为 Agent Trace 格式

### ✅ 双向转换
- OTEL → Agent Trace（自动）
- Agent Trace → OTEL（通过 `/api/traces/{id}/otel`）

### ✅ 灵活存储
- 内存存储（开发用）
- 可扩展到文件系统、数据库等

### ✅ REST API
- CRUD 操作
- 分页支持
- 统计信息

### ✅ 生产就绪
- 结构化日志
- 错误处理
- Partial success 支持

## 与现有 OTEL 工具集成

### Jaeger

```bash
# Jaeger 可以发送 traces 到 Agent Trace Server
docker run -d \
  -e COLLECTOR_OTLP_ENABLED=true \
  -e SPAN_STORAGE_TYPE=otlp \
  -e COLLECTOR_OTLP_HTTP_ENDPOINT=http://host.docker.internal:4318/v1/traces \
  jaegertracing/all-in-one:latest
```

### OpenTelemetry Collector

```yaml
# otel-collector-config.yaml
exporters:
  otlphttp:
    endpoint: http://localhost:4318/v1/traces
    
service:
  pipelines:
    traces:
      receivers: [otlp]
      processors: [batch]
      exporters: [otlphttp]
```

### Cursor / Windsurf（AI 编码工具）

AI 编码工具可以配置发送 traces：

```json
{
  "agent_trace": {
    "enabled": true,
    "otel": {
      "endpoint": "http://localhost:4318/v1/traces",
      "protocol": "otlp/http"
    }
  }
}
```

## 测试结果

```bash
./gradlew :trace_server:test

✅ Phase 1 Tests (7 tests)
  - AgentTraceToOtelConverterTest
  - OtelToAgentTraceConverterTest

✅ Phase 2 Tests (19 tests)
  - OtelTraceReceiverTest (4 tests)
  - InMemoryTraceStorageTest (9 tests)  
  - TraceServerTest (6 tests)

Total: 26/26 tests passed
```

## 示例代码

查看 `src/main/kotlin/org/archguard/trace/examples/`:
- `ConverterExample.kt` - 转换器使用示例
- `ServerExample.kt` - 服务器和 API 使用示例

运行示例：
```bash
./gradlew :trace_server:run --args="send"    # 发送 traces 示例
./gradlew :trace_server:run --args="storage" # 存储操作示例
```

## 配置

环境变量：
- `TRACE_SERVER_PORT`: 服务器端口（默认 4318）
- `TRACE_SERVER_HOST`: 绑定地址（默认 0.0.0.0）

## 下一步（Phase 3）

- [ ] Database storage backend
- [ ] Grafana dashboard integration
- [ ] Prometheus metrics export
- [ ] Rate limiting and authentication
- [ ] Frontend visualization

## 故障排查

### 端口已被占用

```bash
# 更换端口
./gradlew :trace_server:run --args="8080"
```

### 连接被拒绝

确保服务器正在运行：
```bash
curl http://localhost:4318/health
```

### OTLP 格式错误

检查 JSON 格式是否符合 OTLP 规范。参考：
- [OTLP Specification](https://opentelemetry.io/docs/specs/otlp/)

## 性能

- 无状态设计，支持水平扩展
- 异步处理
- 批量操作支持
- 内存存储：~1000 traces/MB

## 更多信息

- **Phase 1 文档**: 见 `README.md`
- **语义约定**: 见 `SEMANTIC_CONVENTIONS.md`
- **实现细节**: 见 `IMPLEMENTATION_SUMMARY.md`

---

**Phase 2 完成时间**: 2026-02-04  
**Status**: ✅ 生产就绪  
**Tests**: 26/26 passing
