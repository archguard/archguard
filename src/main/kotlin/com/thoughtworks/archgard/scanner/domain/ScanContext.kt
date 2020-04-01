package com.thoughtworks.archgard.scanner.domain

import com.thoughtworks.archgard.scanner.domain.config.model.ToolConfigure
import java.io.File

data class ScanContext(var repo: String, val workspace: File, val config: List<ToolConfigure>)