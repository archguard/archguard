package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.archguard.rule.core.Issue
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.diffchanges.ChangedCall
import org.archguard.scanner.core.git.GitLogs
import org.archguard.scanner.core.sca.CompositionDependency
import org.archguard.scanner.core.sourcecode.CodeDatabaseRelation
import org.archguard.scanner.core.sourcecode.ContainerService
import java.io.FileWriter

class ArchGuardCsvClient(private val systemId: String) : ArchGuardClient {
    private fun buildFileName(topic: String): String = systemId + "_" + topic + ".csv"
    private val csvMapper = CsvMapper().apply {
        registerModule(KotlinModule.Builder().build())
    }

    private inline fun <reified T> writeCsvFile(data: List<T>, fileName: String) {
        FileWriter(fileName).use { writer ->
            csvMapper.writer(csvMapper.schemaFor(T::class.java).withHeader())
                .writeValues(writer)
                .writeAll(data)
                .close()
        }
    }

    override fun saveDataStructure(codes: List<CodeDataStruct>) {
        writeCsvFile(codes, buildFileName("codes"))
    }

    override fun saveApi(apis: List<ContainerService>) {
        writeCsvFile(apis, buildFileName("apis"))
    }

    override fun saveRelation(records: List<CodeDatabaseRelation>) {
        writeCsvFile(records, buildFileName("databases"))
    }

    override fun saveGitLogs(gitLogs: List<GitLogs>) {
        writeCsvFile(gitLogs[0].commitLog, buildFileName("gitlogs-commit"))
        writeCsvFile(gitLogs[0].changeEntry, buildFileName("gitlogs-change-entry"))
        writeCsvFile(gitLogs[0].pathChangeCount, buildFileName("gitlogs-change-count"))
    }

    override fun saveDiffs(calls: List<ChangedCall>) {
        writeCsvFile(calls, buildFileName("diff-changes"))
    }

    override fun saveDependencies(dependencies: List<CompositionDependency>) {
        writeCsvFile(dependencies, buildFileName("sca-dependencies"))
    }

    override fun saveRuleIssues(issues: List<Issue>) {
        writeCsvFile(issues, buildFileName("issues"))
    }
}
