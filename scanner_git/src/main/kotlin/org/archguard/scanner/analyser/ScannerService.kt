package org.archguard.scanner.analyser

import org.archguard.scanner.analyser.language.LanguageService
import org.archguard.scanner.analyser.language.LineCounter
import org.archguard.scanner.core.client.dto.ChangeEntry
import org.archguard.scanner.core.client.dto.GitLogs
import org.archguard.scanner.core.client.dto.PathChangeCount
import java.io.File
import java.util.UUID

class ScannerService {
    private val adapter = JGitAdapter()
    private val languageService: LanguageService = LanguageService()

    fun scan(path: String, branch: String, startedAt: Long, repoId: String): GitLogs {
        val (commitLogs, changeEntries) = adapter.scan(path, branch, startedAt, repoId)
        val pathChangeCounts = countFileChanges(changeEntries, path)
        return GitLogs(commitLogs, changeEntries, pathChangeCounts)
    }

    private fun countFileChanges(changeEntries: List<ChangeEntry>, path: String): List<PathChangeCount> {
        val counts = adapter.countChangesByPath(changeEntries)
        val pathChanges = mutableListOf<PathChangeCount>()
        val absolutePath = File(path).absolutePath
        counts.forEach {
            var lang = ""
            var lineCounts: Long = 0
            val filepath = arrayOf(absolutePath, it.key).joinToString(File.separator)

            val countFile = File(filepath)
            if (countFile.isFile) {
                lang = languageService.determineLanguage(countFile.name)
                if (lang.isNotEmpty()) {
                    lineCounts = LineCounter.byPath(filepath)
                }
            }
            pathChanges += PathChangeCount(
                UUID.randomUUID().toString(),
                it.key,
                it.value,
                lineCounts,
                lang,
            )
        }
        return pathChanges
    }
}
