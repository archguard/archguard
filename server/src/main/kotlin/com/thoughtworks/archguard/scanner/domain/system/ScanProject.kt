package com.thoughtworks.archguard.scanner.domain.system

import java.io.File

data class ScanProject(
    val repo: String,
    val workspace: File,
    var buildTool: BuildTool,
    val language: String,
    val codePath: String,
    val branch: String
)
