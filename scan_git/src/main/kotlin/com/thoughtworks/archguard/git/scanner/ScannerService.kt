package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.helper.Bean2Sql
import java.io.File
import java.util.*

/*
* core scanner
*/

class ScannerService(private val gitAdapter: JGitAdapter,
                     private val bean2Sql: Bean2Sql) {

    fun git2SqlFile(gitPath: String, branch: String, after: String, repoId: String, systemId: Long) {
        val (commitLogs, changeEntries) = gitAdapter.scan(gitPath, branch, after, repoId, systemId)
        val file = File("output.sql")
        if (file.exists()) {
            file.delete()
        }

        val counts = gitAdapter.countChangesByPath(changeEntries)
        val pathChanges: MutableList<PathChangeCount> = mutableListOf()
        counts.forEach {
            val lineCounts = LineCounter.byPath(arrayOf(File(gitPath).absolutePath, it.key).joinToString(File.separator))
            pathChanges += PathChangeCount(
                UUID.randomUUID().toString(),
                it.key,
                it.value,
                lineCounts,
                systemId
            )
        }

        file.appendText(commitLogs.joinToString("\n") { bean2Sql.bean2Sql(it) })
        file.appendText(changeEntries.joinToString("\n") { bean2Sql.bean2Sql(it) })
        file.appendText(pathChanges.joinToString("\n") { bean2Sql.bean2Sql(it) })
    }
}
