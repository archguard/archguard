package com.thoughtworks.archguard.analyser

import com.thoughtworks.archguard.analyser.adapter.ScannerService
import org.archguard.scanner.core.client.dto.GitLogs
import org.archguard.scanner.core.git.GitContext

class GitAnalyser(override val context: GitContext) : org.archguard.scanner.core.git.GitAnalyser {
    private val service = ScannerService()

    override fun analyse(): GitLogs {
        val gitLogs = with(context) { service.scan(path, branch, startedAt, repoId) }
        context.client.saveGitLogs(gitLogs)
        return gitLogs
    }
}
