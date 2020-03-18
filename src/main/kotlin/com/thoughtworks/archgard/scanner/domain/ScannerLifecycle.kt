package com.thoughtworks.archgard.scanner.domain

internal interface ScannerLifecycle {
    fun preProcess(context: ScanContext?)
    fun scan(context: ScanContext?)
    fun storeReport(context: ScanContext?)
    fun clean(context: ScanContext?)
}