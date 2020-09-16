package com.thoughtworks.archgard.scanner.domain.scanner.bak.style

data class Style(val id: String, val systemId: Long, val file: String,
                 val source: String, val message: String,
                 val line: Int, val column: Int,
                 val severity: String)