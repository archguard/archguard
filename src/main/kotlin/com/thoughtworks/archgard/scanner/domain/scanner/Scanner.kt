package com.thoughtworks.archgard.scanner.domain.scanner

import com.thoughtworks.archgard.scanner.domain.ScanContext

interface Scanner {
    fun scan(context: ScanContext)
}