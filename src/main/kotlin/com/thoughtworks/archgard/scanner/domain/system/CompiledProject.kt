package com.thoughtworks.archgard.scanner.domain.system

import java.io.File

data class CompiledProject(val repo: String, val workspace: File, var buildTool: BuildTool, val sql: String?)
