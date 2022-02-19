package com.thoughtworks.archgard.scanner.domain.hubexecutor

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.infrastructure.FileOperator

class HubExecutor(private val context: ScanContext, private val manager: ScannerManager) : HubLifecycle {

    override fun execute() {
        manager.execute(context)
    }

    override fun clean() {
        FileOperator.deleteDirectory(context.workspace)
    }
}
