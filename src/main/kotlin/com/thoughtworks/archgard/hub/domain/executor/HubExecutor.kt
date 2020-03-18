package com.thoughtworks.archgard.hub.domain.executor

import com.thoughtworks.archgard.hub.domain.helper.ScannerManager
import com.thoughtworks.archgard.hub.domain.model.HubLifecycle
import com.thoughtworks.archgard.scanner.domain.ScanContext
import org.eclipse.jgit.api.Git
import org.springframework.stereotype.Component
import java.io.File

@Component
class HubExecutor : HubLifecycle {
    override fun projectInfo(context: ScanContext) {
    }

    override fun getSource(context: ScanContext) {
        context.sourcePath = Git.cloneRepository()
                .setGitDir(File(context.workspace))
                .setURI(context.repo)
                .call()
                .repository
                .directory
                .absolutePath
    }

    override fun buildSource(context: ScanContext) {
    }

    override fun getScanner(context: ScanContext, manager: ScannerManager) = manager.load()

    override fun execute(context: ScanContext, manager: ScannerManager) = manager.execute(context)

    override fun clean(context: ScanContext) {
    }
}