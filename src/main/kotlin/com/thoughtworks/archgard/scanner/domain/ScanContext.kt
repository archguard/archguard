package com.thoughtworks.archgard.scanner.domain

import java.io.File

data class ScanContext(var repo: String, val workspace: File, val config: Map<String, Any>)