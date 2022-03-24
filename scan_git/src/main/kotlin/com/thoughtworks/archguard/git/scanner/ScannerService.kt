package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.helper.Bean2Sql
import com.thoughtworks.archguard.git.scanner.model.LineCounter
import java.io.File
import java.util.*

class ScannerService(private val gitAdapter: JGitAdapter, private val bean2Sql: Bean2Sql) {
    private val languageService: LanguageService = LanguageService()

    fun git2SqlFile(gitPath: String, branch: String, after: String, repoId: String, systemId: Long) {
        val (commitLogs, changeEntries) = gitAdapter.scan(gitPath, branch, after, repoId, systemId)
        val file = File("output.sql")
        if (file.exists()) {
            file.delete()
        }

        val pathChanges: MutableList<PathChangeCount> = countFileChanges(changeEntries, gitPath, systemId)

        file.appendText(commitLogs.joinToString("\n") { bean2Sql.bean2Sql(it) })
        file.appendText(changeEntries.joinToString("\n") { bean2Sql.bean2Sql(it) })
        file.appendText(pathChanges.joinToString("\n") { bean2Sql.bean2Sql(it) })
    }

    private fun countFileChanges(
        changeEntries: List<ChangeEntry>,
        workspace: String,
        systemId: Long
    ): MutableList<PathChangeCount> {
        val counts = gitAdapter.countChangesByPath(changeEntries)
        val pathChanges: MutableList<PathChangeCount> = mutableListOf()
        val gitPath = File(workspace)
        counts.forEach {
            var lang = ""
            var lineCounts: Long = 0
            val filepath = arrayOf(gitPath.absolutePath, it.key).joinToString(File.separator)

            val countFile = File(filepath)
            if (countFile.isFile) {
                lang = languageService.detectLanguage(countFile.name)
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
                systemId
            )
        }
        return pathChanges
    }
}
