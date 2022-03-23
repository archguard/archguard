package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.helper.Bean2Sql
import com.thoughtworks.archguard.git.scanner.model.Language
import com.thoughtworks.archguard.git.scanner.model.LineCounter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.*

class ScannerService(private val gitAdapter: JGitAdapter,
                     private val bean2Sql: Bean2Sql) {

    var extToLanguage: MutableMap<String, String> = mutableMapOf()

    fun git2SqlFile(gitPath: String, branch: String, after: String, repoId: String, systemId: Long) {
        val fileContent = this.javaClass.classLoader.getResource("languages.json").readText()

        val languages = Json.decodeFromString<Array<Language>>(fileContent)

        languages.forEach { entry ->
            entry.extensions.forEach {
                extToLanguage[it] = entry.name
            }
        }

        val (commitLogs, changeEntries) = gitAdapter.scan(gitPath, branch, after, repoId, systemId)
        val file = File("output.sql")
        if (file.exists()) {
            file.delete()
        }

        val counts = gitAdapter.countChangesByPath(changeEntries)
        val pathChanges: MutableList<PathChangeCount> = mutableListOf()
        val gitPath = File(gitPath)
        counts.forEach {
            var lang = ""
            var lineCounts: Long = 0
            val filepath = arrayOf(gitPath.absolutePath, it.key).joinToString(File.separator)

            val countFile = File(filepath)
            if(countFile.isFile) {
                val optLang = extToLanguage[countFile.extension]
                if (optLang != null) {
                    lineCounts = LineCounter.byPath(filepath)
                    lang = optLang.toString()
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

        file.appendText(commitLogs.joinToString("\n") { bean2Sql.bean2Sql(it) })
        file.appendText(changeEntries.joinToString("\n") { bean2Sql.bean2Sql(it) })
        file.appendText(pathChanges.joinToString("\n") { bean2Sql.bean2Sql(it) })
    }
}
