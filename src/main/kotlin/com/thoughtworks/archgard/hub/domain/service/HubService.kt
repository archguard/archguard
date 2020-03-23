package com.thoughtworks.archgard.hub.domain.service

import com.thoughtworks.archgard.hub.domain.helper.ScannerManager
import com.thoughtworks.archgard.hub.domain.model.HubLifecycle
import com.thoughtworks.archgard.hub.domain.repository.HubRepository
import com.thoughtworks.archgard.scanner.domain.ScanContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HubService {
    @Autowired
    private lateinit var manager: ScannerManager

    @Autowired
    private lateinit var hubRepository: HubRepository

    @Autowired
    private lateinit var hubExecutor: HubLifecycle

    fun doScan() {
        val gitRepo = hubRepository.getProjectInfo().gitRepo
        val context = ScanContext(gitRepo, createTempDir())

        try {
            hubExecutor.getSource(context)
            hubExecutor.buildSource(context)
            hubExecutor.getScanner(context, manager)
            hubExecutor.execute(context, manager)
        } finally {
            hubExecutor.clean(context)
        }
    }
}