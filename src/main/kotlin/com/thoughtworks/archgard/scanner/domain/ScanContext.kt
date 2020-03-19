package com.thoughtworks.archgard.scanner.domain

import java.io.File

class ScanContext {
    var repo: String = ""
    var workspace: String = ""
    var sourcePath: String = ""
    var projectRoot: File? = null
}