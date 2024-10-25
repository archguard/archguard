package org.archguard.scanner.ctl.client

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.rule.core.Issue
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.context.LanguageEstimate
import org.archguard.context.ChangedCall
import org.archguard.context.GitLogs
import org.archguard.context.ApiCollection
import org.archguard.context.CompositionDependency
import org.archguard.context.CodeDatabaseRelation
import org.archguard.context.ContainerService

open class ArchGuardConsoleClient(private val systemId: String) : ArchGuardClient {

    override fun saveDataStructure(codes: List<CodeDataStruct>) {
        println("==============================================================")
        println("Output code data structures to console")
        println(Json.encodeToString(codes))
    }

    override fun saveApi(apis: List<ContainerService>) {
        println("==============================================================")
        println("Output api container services to console")
        println(Json.encodeToString(apis))
    }

    override fun saveRelation(records: List<CodeDatabaseRelation>) {
        println("==============================================================")
        println("Output database relationships to console")
        println(Json.encodeToString(records))
    }

    override fun saveGitLogs(gitLogs: List<GitLogs>) {
        println("==============================================================")
        println("Output git logs to console")
        println(Json.encodeToString(gitLogs))
    }

    override fun saveDiffs(calls: List<ChangedCall>) {
        println("==============================================================")
        println("Output git diffs to console")
        println(Json.encodeToString(calls))
    }

    override fun saveDependencies(dependencies: List<CompositionDependency>) {
        println("==============================================================")
        println("Output project dependencies to console")
        println(Json.encodeToString(dependencies))
    }

    override fun saveRuleIssues(issues: List<Issue>) {
        println("==============================================================")
        println("Output project dependencies to console")
        println(Json.encodeToString(issues))
    }

    override fun saveEstimates(estimates: List<LanguageEstimate>) {
        println("==============================================================")
        println("Output project estimates to console")
        println(Json.encodeToString(estimates))
    }

    override fun saveOpenApi(collections: List<ApiCollection>) {
        println("==============================================================")
        println("Output project openapi to console")
        println(Json.encodeToString(collections))
    }
}
