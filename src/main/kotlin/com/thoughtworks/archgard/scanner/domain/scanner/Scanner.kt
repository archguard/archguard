package com.thoughtworks.archgard.scanner.domain.scanner

import com.thoughtworks.archgard.scanner.domain.ScanContext

interface Scanner {
    val name: String
    fun scan(context: ScanContext)
}