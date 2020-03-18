package com.thoughtworks.archgard.scanner.domain.toolscanners

import com.thoughtworks.archgard.scanner.domain.ScanContext

interface ScannerLifecycle {
    fun preProcess(context: ScanContext?)
    fun scan(context: ScanContext?)
    fun storeReport(context: ScanContext?)
    fun clean(context: ScanContext?)
}