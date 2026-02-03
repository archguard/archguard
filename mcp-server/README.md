# ArchGuard MCP Server

A lightweight Model Context Protocol (MCP) server that exposes ArchGuard's architecture linter capabilities to AI assistants and development tools.

## Overview

The ArchGuard MCP Server allows AI assistants like Claude, ChatGPT, and development tools to leverage ArchGuard's architectural analysis capabilities through a standardized interface.

## Features

### Resources

- `archguard://rules` - Complete list of all architectural linting rules
- `archguard://rules/{category}` - Rules for specific category (code, webapi, sql, test, layer, comment, protobuf)

### Tools

| Tool | Description |
|------|-------------|
| `lint_code` | Analyze code quality issues |
| `lint_webapi` | Check REST API design |
| `lint_sql` | Validate SQL and database design |
| `lint_test` | Analyze test code quality |
| `lint_layer` | Check layer architecture violations |
| `lint_all` | Run all applicable linters |
| `get_rules` | Get available rules by category |

### Prompts

- `analyze-architecture` - Comprehensive architectural analysis
- `fix-issues` - Review and fix linting issues
- `code-review` - Perform code review with architectural focus
- `api-design-review` - Review REST API design
- `sql-review` - Review SQL and database schema

## Installation

### Build from Source

```bash
./gradlew :mcp-server:shadowJar
```

The fat JAR will be created at `mcp-server/build/libs/archguard-mcp-server.jar`.

## Usage

### With Claude Desktop (stdio mode)

Add to your Claude Desktop configuration (`~/.config/Claude/claude_desktop_config.json`):

```json
{
  "mcpServers": {
    "archguard": {
      "command": "java",
      "args": [
        "-jar",
        "/path/to/archguard-mcp-server.jar"
      ]
    }
  }
}
```

### Standalone (HTTP mode)

```bash
# Start with HTTP transport (binds to localhost:8080 by default)
MCP_MODE=http java -jar archguard-mcp-server.jar

# Or with custom port
MCP_MODE=http MCP_PORT=9000 java -jar archguard-mcp-server.jar
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `MCP_MODE` | Server mode: `stdio`, `http`, `sse` | `stdio` |
| `MCP_PORT` | HTTP port | `8080` |
| `MCP_HOST` | HTTP host binding | `127.0.0.1` |
| `MCP_WORKSPACE` | Default workspace path | Current directory |

## Example Usage

### Lint SQL Statements

```json
{
  "method": "tools/call",
  "params": {
    "name": "lint_sql",
    "arguments": {
      "sql": "CREATE TABLE users (id INT, name VARCHAR);"
    }
  }
}
```

### Lint Web API

```json
{
  "method": "tools/call",
  "params": {
    "name": "lint_webapi",
    "arguments": {
      "path": "/path/to/project",
      "apis": [
        {
          "name": "UserService",
          "resources": [
            {
              "url": "/api/GetUsers",
              "method": "GET"
            }
          ]
        }
      ]
    }
  }
}
```

### Get Rules

```json
{
  "method": "tools/call",
  "params": {
    "name": "get_rules",
    "arguments": {
      "category": "webapi"
    }
  }
}
```

## Security

- **stdio mode (default)**: Secure by design - no network exposure
- **HTTP mode**: Binds to localhost (127.0.0.1) by default
- For remote access, deploy behind an authenticated, TLS-terminating reverse proxy

## Docker

```bash
# Build
docker build -t archguard/mcp-server:latest .

# Run (stdio mode)
docker run -i archguard/mcp-server:latest

# Run (HTTP mode)
docker run -p 127.0.0.1:8080:8080 -e MCP_MODE=http archguard/mcp-server:latest
```

## Development

### Running Tests

```bash
./gradlew :mcp-server:test
```

### Project Structure

```
mcp-server/
├── src/
│   ├── main/kotlin/org/archguard/mcp/
│   │   ├── Main.kt                    # Entry point
│   │   ├── server/
│   │   │   ├── ArchGuardMcpServer.kt  # Server configuration
│   │   │   └── ServerConfig.kt        # Configuration model
│   │   ├── resources/
│   │   │   └── ArchGuardResources.kt  # MCP resources
│   │   ├── tools/
│   │   │   ├── ArchGuardTools.kt      # MCP tools registry
│   │   │   └── LintEngine.kt          # Linting execution
│   │   └── prompts/
│   │       └── ArchGuardPrompts.kt    # MCP prompts
│   └── test/kotlin/org/archguard/mcp/
│       ├── integration/
│       │   └── McpServerIntegrationTest.kt
│       └── resources/
│           └── ArchGuardResourcesTest.kt
└── build.gradle.kts
```

## License

MIT License - See [LICENSE](../LICENSE) for details.
