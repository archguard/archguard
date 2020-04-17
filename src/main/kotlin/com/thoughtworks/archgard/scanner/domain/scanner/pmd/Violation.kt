package com.thoughtworks.archgard.scanner.domain.scanner.pmd


data class Violation(val file: String,
                     val beginline: Int,
                     val endline: Int,
                     val priority: Int,
                     val text: String)