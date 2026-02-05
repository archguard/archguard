#!/usr/bin/env bash
set -euo pipefail

# Smoke test runner for Claude Code OTEL export -> trace_server.
#
# Requirements:
# - trace_server running (default: http://localhost:4318)
# - `claude` available in PATH
# - `expect` installed (macOS: `brew install expect`)
#
# Usage:
#   ./claude-otel-smoketest.sh
#   ENDPOINT=http://localhost:4318 WAIT_SECONDS=10 MESSAGE="hi" ./claude-otel-smoketest.sh

ENDPOINT="${ENDPOINT:-http://localhost:4318}"
TRACES_ENDPOINT="${TRACES_ENDPOINT:-$ENDPOINT/v1/traces}"
WAIT_SECONDS="${WAIT_SECONDS:-10}"
MESSAGE="${MESSAGE:-hi}"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "== Claude OTEL smoketest =="
echo "endpoint:       $ENDPOINT"
echo "traces endpoint:$TRACES_ENDPOINT"
echo "message:        $MESSAGE"
echo "wait seconds:   $WAIT_SECONDS"
echo

echo "Checking trace_server health..."
curl -fsS "$ENDPOINT/health" >/dev/null

before_total="$(curl -fsS "$ENDPOINT/api/traces" | python3 -c 'import json,sys; print(json.load(sys.stdin)["total"])')"
echo "traces before: $before_total"

echo
echo "Running Claude interactive automation..."
chmod +x "$SCRIPT_DIR/claude-otel-interactive.exp" || true
conn_log="/tmp/claude-otel-smoketest.connections.log"
server_log_hint="/tmp/claude-otel-smoketest.server-hint.log"
: >"$conn_log"
: >"$server_log_hint"

# Run expect script in background and sample TCP connections while it runs.
"$SCRIPT_DIR/claude-otel-interactive.exp" \
  --endpoint "$ENDPOINT" \
  --traces-endpoint "$TRACES_ENDPOINT" \
  --message "$MESSAGE" \
  --wait-seconds "$WAIT_SECONDS" &
expect_pid="$!"

echo "Sampling TCP connections to :4318 while Claude runs..."
while kill -0 "$expect_pid" 2>/dev/null; do
  # Best-effort: capture any established connections (claude -> trace_server).
  # If lsof isn't available, ignore.
  lsof -nP -iTCP:4318 -sTCP:ESTABLISHED 2>/dev/null | grep -E "claude|java" >>"$conn_log" || true
  sleep 1
done

wait "$expect_pid" || true

echo
echo "Connection sample log: $conn_log"

echo
after_total="$(curl -fsS "$ENDPOINT/api/traces" | python3 -c 'import json,sys; print(json.load(sys.stdin)["total"])')"
echo "traces after:  $after_total"

if [[ "$after_total" -le "$before_total" ]]; then
  echo
  echo "FAIL: traces did not increase."
  echo "See transcript at: /tmp/claude-otel-interactive.expect.log"
  echo
  echo "Diagnostics:"
  echo "- Connection samples (last 20 lines):"
  tail -n 20 "$conn_log" 2>/dev/null || true
  echo
  echo "- If your trace_server is running via Cursor/Gradle, check its console for:"
  echo "  'Received protobuf OTLP request: ... bytes' or 'Receiving OTLP traces: ...'"
  exit 1
fi

echo
echo "PASS: traces increased ($before_total -> $after_total)."
echo "Latest trace summary:"
curl -fsS "$ENDPOINT/api/traces?offset=0&limit=1" | python3 - <<'PY'
import json,sys
data=json.load(sys.stdin)
tr=data.get("traces") or []
if not tr:
    print("(no traces returned)")
else:
    t=tr[0]
    print(json.dumps({
        "id": t.get("id"),
        "timestamp": t.get("timestamp"),
        "vcs": t.get("vcs"),
        "tool": t.get("tool"),
        "metadata": t.get("metadata"),
    }, indent=2))
PY

