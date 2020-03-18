package com.thoughtworks.archgard.hub.domain.model

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.hub.domain.helper.ScannerManager

interface HubLifecycle {
    fun projectInfo(context: ScanContext)

    fun getSource(context: ScanContext)

    fun buildSource(context: ScanContext)

    fun getScanner(context: ScanContext, manager: ScannerManager)

    fun execute(context: ScanContext, manager: ScannerManager)

    fun clean(context: ScanContext)
}