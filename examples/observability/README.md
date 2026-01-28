# ArchGuard Observability Example

This example demonstrates ArchGuard's runtime observability capabilities with OpenTelemetry integration.

## Architecture

```
┌─────────────────┐
│  Sample App     │ ──► Sends telemetry data
└─────────────────┘
        │
        ▼
┌─────────────────┐
│ ArchGuard       │ ──► Receives & stores OTLP data
│ Backend         │     (HTTP endpoint: /api/telemetry/v1/*)
└─────────────────┘
        │
        ├──► Traces  ──► Jaeger UI (http://localhost:16686)
        ├──► Metrics ──► Prometheus (http://localhost:9090)
        └──► Logs    ──► Grafana (http://localhost:3000)
```

## Components

- **ArchGuard Backend**: OTLP-compatible telemetry ingestion endpoint
- **Jaeger**: Distributed tracing UI
- **Prometheus**: Metrics storage and querying
- **Grafana**: Unified observability dashboard
- **OpenTelemetry Collector**: (Optional) OTLP data collector and forwarder

## Quick Start

### 1. Start the observability stack

```bash
cd examples/observability
docker-compose up -d
```

This starts:
- Jaeger at http://localhost:16686
- Prometheus at http://localhost:9090
- Grafana at http://localhost:3000 (admin/admin)

### 2. Start ArchGuard Backend

```bash
cd ../../
./gradlew :server:bootRun
```

ArchGuard backend will be available at http://localhost:8080

### 3. Run the sample application

```bash
cd examples/observability
./gradlew run
```

Or manually with Kotlin:

```bash
cd sample-app
kotlinc -script send-telemetry.main.kts
```

### 4. View the data

- **Traces**: http://localhost:16686
  - Search for service "archguard-example"
  - View distributed traces and spans

- **Metrics**: http://localhost:9090
  - Query: `archguard_requests_total`
  - Query: `archguard_request_duration_seconds`

- **Logs**: Check ArchGuard backend console output

- **Grafana**: http://localhost:3000
  - Username: admin
  - Password: admin
  - Pre-configured dashboards for traces and metrics

## API Endpoints

ArchGuard exposes OTLP-compatible endpoints:

- `POST /api/telemetry/v1/traces` - Ingest distributed traces
- `POST /api/telemetry/v1/metrics` - Ingest metrics (counters, gauges, histograms)
- `POST /api/telemetry/v1/logs` - Ingest structured logs
- `GET /api/telemetry/health` - Health check

## Configuration

### Environment Variables

- `ARCHGUARD_BACKEND_URL`: ArchGuard backend URL (default: http://localhost:8080)
- `OTEL_EXPORTER_OTLP_ENDPOINT`: OTLP collector endpoint (default: http://localhost:4318)

### Docker Compose Services

Edit `docker-compose.yml` to customize:
- Ports
- Resource limits
- Retention periods
- Storage backends

## Development

### Adding Custom Metrics

```kotlin
val counter = Counter(
    name = "custom_metric",
    value = 1,
    timeUnixNano = System.currentTimeMillis() * 1_000_000
)
```

### Creating Custom Traces

```kotlin
val span = Span(
    context = TraceContext(
        traceId = UUID.randomUUID().toString().replace("-", ""),
        spanId = UUID.randomUUID().toString().replace("-", "").substring(0, 16)
    ),
    name = "custom-operation",
    kind = SpanKind.INTERNAL,
    startTimeUnixNano = startNano,
    endTimeUnixNano = endNano,
    status = SpanStatus(StatusCode.OK)
)
```

## Troubleshooting

### No data in Jaeger

1. Check ArchGuard backend logs for ingestion errors
2. Verify sample app successfully sent data
3. Check Jaeger query service is running: `docker ps | grep jaeger`

### Prometheus not scraping metrics

1. Check Prometheus targets: http://localhost:9090/targets
2. Verify ArchGuard metrics endpoint: http://localhost:8080/actuator/prometheus
3. Check `prometheus.yml` configuration

### Connection refused errors

1. Ensure all services are running: `docker-compose ps`
2. Check network connectivity: `docker network inspect observability_default`
3. Verify ports are not in use: `lsof -i :16686,9090,3000`

## Clean Up

```bash
docker-compose down -v
```

This removes all containers and volumes.

## Next Steps

- Explore Grafana dashboards
- Create custom alerts in Prometheus
- Integrate with your own services
- Export data to long-term storage

## References

- [OpenTelemetry Specification](https://opentelemetry.io/docs/specs/otel/)
- [OTLP Protocol](https://opentelemetry.io/docs/specs/otlp/)
- [Jaeger Documentation](https://www.jaegertracing.io/docs/)
- [Prometheus Documentation](https://prometheus.io/docs/)
- [Grafana Documentation](https://grafana.com/docs/)
