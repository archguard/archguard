# ArchGuard MCP Server Documentation

This directory contains comprehensive documentation for the proposed Model Context Protocol (MCP) server implementation for ArchGuard.

## Documents

### 📋 [mcp-server-proposal.md](./mcp-server-proposal.md)
**Comprehensive proposal document** covering:
- Executive summary and background
- MCP protocol overview and benefits
- Proposed architecture and design
- MCP server specification (Resources, Tools, Prompts)
- Implementation roadmap (3 phases, 6 weeks)
- Technical decisions and alternatives considered
- Success metrics and risk mitigation
- Example configurations and usage scenarios

**Target Audience**: Project maintainers, architects, stakeholders

### 🐛 [mcp-server-issue.md](./mcp-server-issue.md)
**GitHub issue template** ready to submit, including:
- Feature request summary with clear motivation
- Proposed solution with benefits
- Example usage scenarios
- Implementation phases and timeline
- Success metrics
- References and related issues

**Target Audience**: GitHub contributors, project managers

### 🔧 [mcp-server-implementation-guide.md](./mcp-server-implementation-guide.md)
**Step-by-step implementation guide** with:
- Project structure and module setup
- Complete code examples for all components
- MCP protocol handler implementation
- Resource, Tool, and Prompt implementations
- Server setup (stdio and HTTP modes)
- Testing strategies
- Build, package, and deployment instructions
- Usage examples (Claude Desktop, Docker, HTTP API)

**Target Audience**: Developers, implementers

## Quick Links

### Understanding MCP
- [Model Context Protocol Specification](https://modelcontextprotocol.io/)
- [MCP TypeScript SDK](https://github.com/modelcontextprotocol/typescript-sdk)
- [Anthropic MCP Announcement](https://www.anthropic.com/news/model-context-protocol)

### ArchGuard Resources
- [ArchGuard Main Repository](https://github.com/archguard/archguard)
- [ArchGuard Documentation](https://archguard.org/)
- [ArchGuard Roadmap](https://github.com/archguard/archguard/discussions/5)

## Overview

The MCP server proposal aims to expose ArchGuard's architecture linter through a lightweight, AI-friendly interface that enables:

1. **AI-Powered Code Review**: Let AI assistants use ArchGuard to analyze code
2. **IDE Integration**: Real-time linting feedback in development environments
3. **CI/CD Pipeline**: Automated architecture checks in deployment workflows
4. **Lightweight Deployment**: No database, <100MB memory, <2s startup

## Key Features

### Resources
- `archguard://rules` - Complete list of available linting rules
- `archguard://rules/{category}` - Category-specific rules (code, webapi, sql, etc.)

### Tools
- `lint_code` - Analyze code quality and architecture
- `lint_webapi` - Check REST API design patterns
- `lint_sql` - Validate database and SQL design
- `lint_test` - Analyze test code quality
- `lint_layer` - Check layer architecture compliance
- `lint_all` - Run all applicable linters

### Prompts
- `analyze-architecture` - Full architectural analysis template
- `fix-issues` - Code review and fix suggestion template

## Implementation Status

| Phase | Description | Status |
|-------|-------------|--------|
| Phase 0 | Documentation and Proposal | ✅ Complete |
| Phase 1 | Core MCP Server (2 weeks) | 📝 Planned |
| Phase 2 | Enhanced Capabilities (2 weeks) | 📝 Planned |
| Phase 3 | Advanced Features (2 weeks) | 📝 Planned |

## Getting Started (Future)

Once implemented, the MCP server will be available through:

### Maven Central
```kotlin
implementation("org.archguard:mcp-server:1.0.0")
```

### Docker Hub
```bash
docker pull archguard/mcp-server:latest
```

### Binary Distribution
Download from [GitHub Releases](https://github.com/archguard/archguard/releases)

## Architecture Diagram

```
┌─────────────────────────────────────────────┐
│         AI Assistant / Tool                  │
│      (Claude, VSCode, ChatGPT, etc.)        │
└─────────────────┬───────────────────────────┘
                  │ MCP Protocol (JSON-RPC)
┌─────────────────▼───────────────────────────┐
│         ArchGuard MCP Server                 │
│    (Lightweight Ktor-based Server)          │
└─────────────────┬───────────────────────────┘
                  │
┌─────────────────▼───────────────────────────┐
│    Existing ArchGuard Linter Components     │
│  rule-code | rule-webapi | rule-sql | ...   │
└─────────────────────────────────────────────┘
```

## Use Cases

### 1. AI Assistant Integration
```
Developer: "Review my API design"
Claude: [Uses lint_webapi via MCP]
Claude: "Found 5 issues: URLs should be lowercase, avoid CRUD in endpoints..."
```

### 2. IDE Real-time Linting
VSCode Extension connects to MCP server for instant architectural feedback.

### 3. CI/CD Pipeline
GitHub Actions uses MCP server to validate architecture before merge.

## Technical Stack

- **Language**: Kotlin
- **Server**: Ktor (lightweight HTTP)
- **Protocol**: JSON-RPC 2.0 (MCP standard)
- **Transport**: stdio (local) and HTTP (remote)
- **Serialization**: kotlinx.serialization
- **Dependencies**: Existing rule-core and rule-linter modules

## Next Steps

1. **Review**: Team discussion of proposal (see [mcp-server-proposal.md](./mcp-server-proposal.md))
2. **Approval**: Decision to proceed with implementation
3. **Prototype**: Build basic MCP server (1 week)
4. **Validation**: Test with Claude/ChatGPT (1 week)
5. **Implementation**: Follow the [implementation guide](./mcp-server-implementation-guide.md)

## Contributing

If you're interested in contributing to this feature:

1. Review the proposal document
2. Join the discussion (GitHub issue to be created)
3. Check the implementation guide for technical details
4. Submit PRs following the roadmap phases

## Questions?

- Open an issue on GitHub
- Join the ArchGuard community (see main README)
- Contact: `phodal02` (WeChat) or h@phodal.com

---

**Last Updated**: 2026-02-02  
**Status**: Proposal Phase  
**Next Milestone**: Team Review & Approval
