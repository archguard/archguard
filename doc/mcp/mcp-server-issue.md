# GitHub Issue: Expose ArchGuard Arch Linter as MCP Server

## Issue Title
[Feature Request] Expose Arch Linter as Model Context Protocol (MCP) Server

## Issue Labels
- `enhancement`
- `feature-request`
- `architecture`
- `ai-integration`

## Issue Description

### Summary

Create a lightweight Model Context Protocol (MCP) server that exposes ArchGuard's architecture linter capabilities to AI assistants and development tools.

### Motivation

Currently, ArchGuard's powerful architecture linting capabilities are available through:
1. CLI tool (requires local installation)
2. Spring Boot server (heavyweight, requires database)
3. Gradle plugin (build-time only)

None of these options are suitable for modern AI-assisted development workflows where:
- AI assistants like Claude, ChatGPT, and GitHub Copilot need quick access to linting capabilities
- Cloud-based tools cannot easily install CLI dependencies
- Developers want real-time architectural guidance without running full builds
- Lightweight, stateless analysis is preferred over persistent storage

### Proposed Solution

Implement a standalone MCP server that:
- **Wraps existing linter modules**: Reuses rule-core and rule-linter without modification
- **Lightweight architecture**: Kotlin + Ktor, <100MB memory, <2s startup
- **Stateless operation**: No database, processes files directly
- **Standard protocol**: Implements MCP specification for broad compatibility
- **Multiple transports**: Supports both stdio (local) and HTTP (remote)

### MCP Protocol Integration

The MCP server would expose:

**Resources:**
- `archguard://rules` - List all available linting rules
- `archguard://rules/{category}` - Rules for specific category

**Tools:**
- `lint_code` - Analyze code quality issues
- `lint_webapi` - Check REST API design
- `lint_sql` - Validate SQL and database design
- `lint_test` - Analyze test code quality
- `lint_layer` - Check layer architecture
- `lint_all` - Run all applicable linters

**Prompts:**
- `analyze-architecture` - Template for full architectural analysis
- `fix-issues` - Template for reviewing and fixing linting issues

### Architecture

```
AI Assistant (Claude/ChatGPT/etc.)
    ↓ MCP Protocol (JSON-RPC 2.0)
ArchGuard MCP Server (Ktor)
    ↓ Reuses existing components
Linter Core (rule-core, rule-linter/*)
```

### Benefits

**For Users:**
- ✅ AI-powered architecture reviews in IDEs and chat interfaces
- ✅ Instant feedback without build/deployment overhead
- ✅ Works with any MCP-compatible tool
- ✅ No infrastructure setup required

**For ArchGuard:**
- ✅ Wider adoption through AI tool ecosystem
- ✅ Modern interface aligned with industry trends
- ✅ Complementary to existing server (not a replacement)
- ✅ Minimal maintenance (reuses existing linter code)

### Technical Approach

**Implementation:**
1. Create new `mcp-server` module in the repository
2. Implement MCP protocol handler (JSON-RPC 2.0)
3. Integrate with existing RuleSetProvider and AnalyserDispatcher
4. Package as standalone JAR and Docker image

**Dependencies:**
- Existing: rule-core, rule-linter modules
- New: Ktor (lightweight HTTP), kotlinx.serialization (already used)
- No database required
- Authentication: Designed for single-user local use (stdio mode); HTTP mode must use localhost binding or be deployed behind an authenticated, TLS-terminating reverse proxy for any network/multi-user access

**Key Design Principles:**
- ⚡ **Fast**: <2s startup, <5s analysis for 1000 files
- 🪶 **Lightweight**: <100MB memory footprint
- 🔄 **Stateless**: No persistent storage, purely request/response
- 🔌 **Pluggable**: Works with existing rule plugin system
- 📦 **Standalone**: Single JAR or Docker container
- 🔒 **Secure by Default**: stdio mode (local-only) is default; HTTP mode binds to localhost

### Example Usage

**With Claude Desktop:**
```json
{
  "mcpServers": {
    "archguard": {
      "command": "java",
      "args": ["-jar", "/path/to/archguard-mcp-server.jar"]
    }
  }
}
```

**User interaction:**
```
User: "Review the API design in my Spring Boot project"
Claude: [Uses lint_webapi tool via MCP]
Claude: "I found 15 API design issues:
1. POST /api/user/delete - HTTP method should not be in URL
2. GET /api/GetUsers - URL should use lowercase
..."
```

### Alternatives Considered

1. **Extend existing Spring Boot server** ❌
   - Too heavyweight (500MB+, slow startup)
   - Requires database and authentication
   - Not suitable for lightweight MCP integration

2. **Language Server Protocol (LSP)** ❌
   - IDE-specific, doesn't work with AI assistants
   - More complex state management required
   - Limited to editor integration

3. **gRPC API** ❌
   - Less accessible than HTTP/JSON
   - No standard AI tool integration
   - Additional complexity

### Implementation Phases

**Phase 1: Core MCP Server (2 weeks)**
- MCP protocol handler (stdio + HTTP)
- Basic tools: lint_code, lint_webapi, lint_sql
- Resource: archguard://rules
- Documentation and examples

**Phase 2: Complete Integration (2 weeks)**
- All linter types (test, layer, comment, protobuf)
- Configuration file support
- Docker image and deployment guide
- Performance optimization

**Phase 3: Advanced Features (2 weeks)**
- Caching for repeated analyses
- Incremental analysis (only changed files)
- Integration with Architecture-as-Code DSL
- CI/CD integration examples

### Success Metrics

**Technical:**
- Startup time: <2 seconds
- Analysis time: <5 seconds for 1000 files
- Memory usage: <100MB
- Response time: <100ms for metadata queries

**Adoption:**
- Integration with 2+ AI assistants
- 100+ GitHub stars on module
- 50+ downloads/week

### References

- [Model Context Protocol Specification](https://modelcontextprotocol.io/)
- [MCP TypeScript SDK](https://github.com/modelcontextprotocol/typescript-sdk)
- [Anthropic MCP Announcement](https://www.anthropic.com/news/model-context-protocol)
- [Detailed Proposal Document](mcp-server-proposal.md)

### Related Issues

- #5 - ArchGuard Roadmap
- Related to Architecture-as-Code improvements

### Additional Context

This feature would position ArchGuard at the forefront of AI-assisted architecture governance, making it easy for developers to get instant architectural feedback through their favorite AI tools.

The implementation leverages all existing linter code - this is purely a new interface/protocol layer, not a rewrite.

---

**Issue Type:** Feature Request  
**Priority:** High  
**Complexity:** Medium  
**Timeline:** 6 weeks  
**Breaking Changes:** None (pure addition)
