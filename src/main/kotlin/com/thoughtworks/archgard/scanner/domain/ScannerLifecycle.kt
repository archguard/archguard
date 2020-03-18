package com.thoughtworks.archgard.scanner.domain

internal interface ScannerLifecycle {
    fun preproccess(context: ScanContext?)
    fun scan(context: ScanContext?)
    fun storeReport(context: ScanContext?)
    fun clean(context: ScanContext?)
}