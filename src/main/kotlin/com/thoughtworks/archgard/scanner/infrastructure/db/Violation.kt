package com.thoughtworks.archgard.scanner.infrastructure.db


data class Violation(val systemId: Long,
                     val file: String,
                     val beginline: Int,
                     val endline: Int,
                     val priority: Int,
                     val text: String)