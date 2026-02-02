# Proposal: Expose ArchGuard Arch Linter as MCP Server

## Executive Summary

This document proposes exposing the ArchGuard architecture linter as a Model Context Protocol (MCP) server, enabling AI assistants and development tools to leverage ArchGuard's architectural analysis capabilities through a standardized interface.

## Background

### What is MCP?

Model Context Protocol (MCP) is an open protocol developed by Anthropic that enables AI assistants to securely connect with various data sources and tools. MCP provides:

- **Standardized Interface**: Consistent API for tools and resources
- **Resource Management**: Access to contextual information (files, databases, etc.)
- **Tool Invocation**: Execute specific functions/commands
- **Prompts**: Pre-defined prompt templates for common tasks

### Current State of ArchGuard Linter

ArchGuard's rule-based linter is currently available through:

1. **CLI Interface** (`scanner_cli`): Command-line tool for local analysis
2. **HTTP Backend** (`server`): Full-featured Spring Boot server with web UI
3. **Gradle Plugin**: Build-time integration

The linter provides architectural governance across multiple domains:

- **Code Quality**: Code smells, anti-patterns
- **Web API Design**: REST API conventions, naming rules
- **SQL Quality**: Database design, query optimization
- **Test Quality**: Test smells, coverage patterns
- **Layer Architecture**: Dependency violations, layer rules
- **Documentation**: Comment quality, documentation coverage
- **Protobuf**: Protocol Buffer design rules

### The Problem

Current access methods have limitations:

1. **CLI**: Requires local installation, not suitable for cloud-based AI assistants
2. **HTTP Server**: Heavy infrastructure (Spring Boot, database, authentication)
3. **No Standardized AI Integration**: No easy way for AI assistants to leverage linter capabilities

## Proposed Solution: Lightweight MCP Server

### Architecture Overview

```
┌─────────────────────────────────────────────┐
│         AI Assistant / Tool                  │
│      (Claude, VSCode, ChatGPT, etc.)        │
└─────────────────┬───────────────────────────┘
                  │ MCP Protocol
                  │ (JSON-RPC 2.0)
┌─────────────────▼───────────────────────────┐
│         ArchGuard MCP Server                 │
│  ┌─────────────────────────────────────┐   │
│  │    MCP Protocol Handler              │   │
│  │  - Resources (rule definitions)      │   │
│  │  - Tools (lint operations)           │   │
│  │  - Prompts (templates)               │   │
│  └──────────────┬───────────────────────┘   │
│                 │                             │
│  ┌──────────────▼───────────────────────┐   │
│  │    Linter Core Facade                │   │
│  │  - RuleSetProvider Registry          │   │
│  │  - AnalyserDispatcher Integration    │   │
│  │  - Result Formatting                 │   │
│  └──────────────┬───────────────────────┘   │
└─────────────────┼───────────────────────────┘
                  │
┌─────────────────▼───────────────────────────┐
│    Existing ArchGuard Linter Components     │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  │
│  │ rule-code│  │rule-webapi│  │ rule-sql│  │
│  └──────────┘  └──────────┘  └──────────┘  │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  │
│  │rule-test │  │rule-layer│  │rule-proto│  │
│  └──────────┘  └──────────┘  └──────────┘  │
└─────────────────────────────────────────────┘
```

### Implementation Approach: Lightweight vs. Full Server

**Lightweight MCP Server** (Recommended):

- **Technology**: Kotlin with Ktor or Vert.x (lightweight HTTP/WebSocket)
- **Dependencies**: Only rule-core and rule-linter modules
- **No Database**: Stateless, processes files directly
- **No Authentication**: Local/trusted network deployment
- **Resource Usage**: ~50-100MB memory, fast startup (<2s)
- **Deployment**: Single JAR, Docker container, or native binary

**Why Not Use Existing Server?**

The current Spring Boot server is designed for:
- Multi-user web application with authentication
- Persistent storage of scan results
- Complex visualization and reporting
- System-wide architectural analysis

These features are unnecessary for MCP integration where:
- AI assistant needs quick, stateless analysis
- Results are consumed immediately, not stored
- Simple JSON responses are sufficient
- Low latency and resource usage are critical

## MCP Server Specification

### 1. Resources

Resources provide context and information about available linting capabilities.

#### \`archguard://rules\`
Returns all available rule sets and their configurations.

\`\`\`json
{
  "uri": "archguard://rules",
  "name": "Available Linting Rules",
  "description": "Complete list of architectural rules",
  "mimeType": "application/json",
  "contents": [
    {
      "category": "code",
      "rules": [
        {
          "id": "service-should-use-domain-model",
          "name": "Service Should Use Domain Model",
          "description": "Service layer should use domain objects, not data layer objects",
          "severity": "WARN"
        }
      ]
    },
    {
      "category": "webapi",
      "rules": [
        {
          "id": "no-http-method-in-url",
          "name": "No HTTP Method in URL",
          "description": "HTTP method should not be part of the URL path",
          "severity": "ERROR"
        }
      ]
    }
  ]
}
\`\`\`

#### \`archguard://rules/{category}\`
Returns rules for a specific category (code, webapi, sql, test, layer, comment, protobuf).

### 2. Tools

Tools enable the AI assistant to perform linting operations.

#### \`lint_code\`
Analyzes code quality issues.

**Input:**
\`\`\`json
{
  "path": "/path/to/project",
  "language": "java",
  "rules": ["all"],
  "files": ["src/main/java/**/*.java"]
}
\`\`\`

**Output:**
\`\`\`json
{
  "issues": [
    {
      "ruleId": "service-should-use-domain-model",
      "severity": "WARN",
      "message": "Service class should use domain model instead of repository entities",
      "file": "src/main/java/com/example/UserService.java",
      "line": 42,
      "column": 10
    }
  ],
  "summary": {
    "totalIssues": 1,
    "byRule": {
      "service-should-use-domain-model": 1
    },
    "bySeverity": {
      "ERROR": 0,
      "WARN": 1,
      "INFO": 0
    }
  }
}
\`\`\`

#### \`lint_webapi\`
Analyzes REST API design issues.

#### \`lint_sql\`
Analyzes SQL and database design issues.

#### \`lint_test\`
Analyzes test code quality.

#### \`lint_layer\`
Checks layer architecture violations.

#### \`lint_all\`
Runs all applicable linters on a project.

### 3. Prompts

Pre-defined prompt templates for common architectural analysis tasks.

#### \`analyze-architecture\`
\`\`\`
Analyze the architecture of the codebase at {path}. 
Focus on: {aspects}
- Code quality and smells
- API design patterns
- Layer architecture
- Test coverage and quality
\`\`\`

#### \`fix-issues\`
\`\`\`
Review the linting issues in {file}:
{issues}

Suggest fixes for each issue with code examples.
\`\`\`

## Implementation Roadmap

### Phase 1: Core MCP Server (Week 1-2)

**Deliverables:**
- Standalone Kotlin module: \`mcp-server\`
- MCP protocol handler (JSON-RPC 2.0 over stdio/HTTP)
- Integration with existing rule-core and rule-linter
- Basic tools: \`lint_code\`, \`lint_webapi\`, \`lint_sql\`
- Resource endpoint: \`archguard://rules\`

**Dependencies:**
\`\`\`kotlin
dependencies {
    implementation(project(":rule-core"))
    implementation(project(":rule-linter:rule-code"))
    implementation(project(":rule-linter:rule-webapi"))
    implementation(project(":rule-linter:rule-sql"))
    implementation(project(":rule-linter:rule-test"))
    implementation(project(":rule-linter:rule-layer"))
    
    // Lightweight server
    implementation("io.ktor:ktor-server-core:2.3.+")
    implementation("io.ktor:ktor-server-netty:2.3.+")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.+")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.+")
}
\`\`\`

### Phase 2: Enhanced Capabilities (Week 3-4)

**Deliverables:**
- All linter types integrated (test, layer, comment, protobuf)
- Streaming results for large codebases
- Configuration file support (.archguard.yml)
- Docker image and Helm chart
- Documentation and examples

### Phase 3: Advanced Features (Week 5-6)

**Deliverables:**
- Caching for repeated analyses
- Incremental analysis (only changed files)
- Custom rule definition via MCP
- Integration with Architecture-as-Code DSL
- Performance optimization (<1s for small projects)

## Technical Decisions

### Communication Protocol

**Option 1: Standard Input/Output (stdio)** - Recommended for local use
- Pros: Simple, no network configuration, secure by default
- Cons: Limited to local processes
- Use case: VSCode extensions, local CLI tools

**Option 2: HTTP/WebSocket**
- Pros: Remote access, browser-based tools
- Cons: Requires port binding, authentication needed
- Use case: Cloud-based AI assistants, team deployments

**Decision**: Support both, with stdio as default

### Serialization Format

- **Input/Output**: JSON (MCP standard)
- **Internal**: Kotlinx.serialization (already used by rule-core)

### Error Handling

Follow MCP error codes:
- \`-32700\`: Parse error
- \`-32600\`: Invalid request
- \`-32601\`: Method not found
- \`-32602\`: Invalid params
- \`-32603\`: Internal error
- \`-32000 to -32099\`: Custom errors

## Example Usage Scenarios

### Scenario 1: AI Assistant Code Review

\`\`\`
User: "Review the API design in this project"

AI Assistant: [Calls lint_webapi tool via MCP]

MCP Server: Returns 15 API design issues

AI Assistant: "I found 15 API design issues:
1. POST /api/user/delete - HTTP method should not be in URL
2. GET /api/GetUsers - URL should be lowercase
3. POST /api/user with 8 parameters - too many parameters
..."
\`\`\`

### Scenario 2: IDE Integration

\`\`\`
VSCode Extension -> MCP Server -> lint_code
   ↓
Real-time linting feedback in editor
Inline suggestions for architectural improvements
\`\`\`

### Scenario 3: CI/CD Pipeline

\`\`\`
GitHub Actions -> MCP Server (Docker) -> lint_all
   ↓
Comment on PR with architectural issues
Block merge if critical violations found
\`\`\`

## Benefits

### For Users

1. **AI-Powered Architecture Review**: Leverage Claude, ChatGPT, etc. for architectural guidance
2. **Faster Feedback**: Quick analysis without full server deployment
3. **Standardized Integration**: Works with any MCP-compatible tool
4. **Lightweight**: Minimal resource usage, fast startup

### For ArchGuard Project

1. **Wider Adoption**: Easy integration with popular AI tools
2. **Modern Interface**: Aligns with industry trends (AI-assisted development)
3. **Complementary**: Doesn't replace existing server, adds new use case
4. **Open Ecosystem**: MCP's openness attracts contributions

## Risks and Mitigations

### Risk 1: MCP Specification Changes
- **Mitigation**: Use versioned API, abstract MCP protocol layer

### Risk 2: Performance with Large Codebases
- **Mitigation**: Streaming results, incremental analysis, caching

### Risk 3: Limited MCP Adoption
- **Mitigation**: Server also works as standalone HTTP API, not MCP-only

### Risk 4: Maintenance Burden
- **Mitigation**: Share code with existing linter, minimal additional code

## Success Metrics

### Technical Metrics
- Startup time: <2 seconds
- Analysis time: <5 seconds for 1000 files
- Memory usage: <100MB for typical project
- Response time: <100ms for rule metadata queries

### Adoption Metrics
- Integration with at least 2 AI assistants (Claude, ChatGPT plugins)
- 100+ GitHub stars on MCP server module
- 50+ downloads/week of standalone JAR

## Alternatives Considered

### Alternative 1: Extend Existing Server
- **Rejected**: Too heavyweight, requires database, authentication
- **Overhead**: 500MB+ memory, slow startup (10s+)

### Alternative 2: Language Server Protocol (LSP)
- **Rejected**: LSP is IDE-specific, MCP is broader (AI assistants, CLI tools)
- **Complexity**: LSP requires maintaining edit states, cursor positions

### Alternative 3: gRPC API
- **Rejected**: Less accessible than HTTP/JSON, no standard AI integration
- **Adoption**: Fewer tools support gRPC compared to HTTP

## Next Steps

1. **Proposal Review**: Team discussion and feedback (1 week)
2. **Prototype Development**: Basic MCP server with 2-3 tools (1 week)
3. **Demo and Validation**: Test with Claude Code Interpreter (1 week)
4. **Full Implementation**: Follow roadmap Phase 1-3 (6 weeks)
5. **Documentation**: User guide, API reference, examples (ongoing)
6. **Release**: Package and publish to Maven Central, Docker Hub

## References

- [Model Context Protocol Specification](https://modelcontextprotocol.io/)
- [MCP TypeScript SDK](https://github.com/modelcontextprotocol/typescript-sdk)
- [ArchGuard Architecture](https://archguard.org/)
- [Anthropic MCP Announcement](https://www.anthropic.com/news/model-context-protocol)

## Appendix: Example Configuration

### \`.archguard-mcp.yml\`
\`\`\`yaml
server:
  mode: stdio  # or http
  port: 8080  # if http mode
  
linter:
  rules:
    code:
      enabled: true
      severity: warn
    webapi:
      enabled: true
      severity: error
    sql:
      enabled: true
    test:
      enabled: false  # disable test linting
      
  exclude:
    - "**/test/**"
    - "**/build/**"
    - "**/node_modules/**"
    
  languages:
    - java
    - kotlin
    - typescript
\`\`\`

### Docker Compose Example
\`\`\`yaml
version: '3.8'
services:
  archguard-mcp:
    image: archguard/mcp-server:latest
    volumes:
      - ./project:/workspace
    environment:
      - MCP_MODE=http
      - MCP_PORT=8080
    ports:
      - "8080:8080"
\`\`\`

---

**Document Version**: 1.0  
**Author**: ArchGuard Team  
**Date**: 2026-02-02  
**Status**: Proposed
