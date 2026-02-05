## `trace_server/scripts/`

This folder contains **repeatable smoke tests** for validating that Claude Code can export OpenTelemetry telemetry to `trace_server`.

Important: Claude Code exports **metrics** and **logs/events** (not spans/traces).

### Files

- `claude-otel-smoketest.sh`
  - End-to-end runner: checks server health, runs Claude in interactive mode via `expect`, and validates OTLP metrics/logs counters.
- `claude-otel-interactive.exp`
  - The `expect` automation that drives Claude's interactive TUI.
  - It is intentionally tolerant (TUI output changes across Claude versions).

### Prerequisites

- `trace_server` running locally (default `http://localhost:4318`)
- `claude` in `$PATH`
- `expect` installed
  - macOS: `brew install expect`

### Run

```bash
cd trace_server/scripts
./claude-otel-smoketest.sh
```

### Customize

```bash
ENDPOINT=http://localhost:4318 \
TRACES_ENDPOINT=http://localhost:4318/v1/traces \
WAIT_SECONDS=10 \
MESSAGE="hi" \
./claude-otel-smoketest.sh
```

### Logs & Debugging

- **Claude TUI transcript**: `/tmp/claude-otel-interactive.expect.log`
- **TCP connection samples**: `/tmp/claude-otel-smoketest.connections.log`

If traces don't increase, check the `trace_server` console for lines like:

- `Received protobuf OTLP request: ... bytes`
- `Receiving OTLP traces: ... resource spans`

