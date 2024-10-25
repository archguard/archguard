package org.archguard.scanner.analyser

import org.archguard.model.GitLogs
import org.archguard.scanner.core.git.GitContext

class GitAnalyser(override val context: GitContext) : org.archguard.scanner.core.git.GitAnalyser {
    private val service = ScannerService()

    override fun analyse(): List<GitLogs> {
        val gitLogs = with(context) { service.scan(path, branch, startedAt, repoId) }
        val logs = listOf(gitLogs)

        context.client.saveGitLogs(logs)
        return logs
    }
}
