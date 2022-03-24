package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.helper.Bean2Sql
import com.thoughtworks.archguard.git.scanner.model.Language
import com.thoughtworks.archguard.git.scanner.model.LineCounter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.*

class ScannerService(
    private val gitAdapter: JGitAdapter,
    private val bean2Sql: Bean2Sql
) {

    var extToLanguage: MutableMap<String, String> = mutableMapOf()
    var filenameToLanguage: MutableMap<String, String> = mutableMapOf()

    fun git2SqlFile(gitPath: String, branch: String, after: String, repoId: String, systemId: Long) {
        setupLanguagesMap()

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
                lang = languageByFile(countFile)
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

    private fun languageByFile(countFile: File): String {
        val language = ""
        val optFilenameLang = filenameToLanguage[countFile.name.lowercase()]
        if (optFilenameLang != null) {
            return optFilenameLang.toString()
        }

        val optLang = extToLanguage[countFile.extension.lowercase()]
        if (optLang != null) {
            return optLang.toString()
        }
        return language
    }

    private fun setupLanguagesMap() {
        val fileContent = this.javaClass.classLoader.getResource("languages.json").readText()
        val languages = Json.decodeFromString<Array<Language>>(fileContent)
        languages.forEach { entry ->
            entry.extensions.forEach {
                extToLanguage[it] = entry.name
            }
            entry.fileNames?.forEach {
                filenameToLanguage[it] = entry.name
            }
        }
    }
}
