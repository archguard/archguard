package com.thoughtworks.archgard.scanner.domain.scanner.style

data class Style(val id: String, val file: String,
                 val source: String, val message: String,
                 val line: Int, val column: Int,
                 val severity: String)