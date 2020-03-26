package com.thoughtworks.archgard.scanner.domain.scanner.checkstyle

data class CheckStyle(var id: String, var file: String, var source: String, var message: String, var line: Int, var column: Int, var severity: String)