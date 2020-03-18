package com.thoughtworks.archgard.hub.domain.service

import com.thoughtworks.archgard.hub.domain.model.HubLifecycle
import com.thoughtworks.archgard.hub.domain.helper.ScannerManager
import com.thoughtworks.archgard.scanner.domain.ScanContext
import org.springframework.beans.factory.annotation.Autowired

class HubService {
    @Autowired
    lateinit var context: ScanContext

    @Autowired
    lateinit var manager: ScannerManager

    @Autowired
    lateinit var hubExecutor: HubLifecycle

    fun doScan() {
        hubExecutor.projectInfo(context)
        hubExecutor.getSource(context)
        hubExecutor.buildSource(context)
        hubExecutor.getScanner(context, manager)
        hubExecutor.execute(context, manager)
        hubExecutor.clean(context)
    }
}