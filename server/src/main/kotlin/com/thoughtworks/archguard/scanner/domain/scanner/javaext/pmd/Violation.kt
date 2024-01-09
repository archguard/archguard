package com.thoughtworks.archguard.scanner.domain.scanner.javaext.pmd

data class Violation(
    val systemId: Long,
    val file: String,
    val beginline: Int,
    val endline: Int,
    val priority: Int,
    val text: String
)
