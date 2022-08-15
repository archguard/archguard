package com.thoughtworks.archguard.smartscanner.controller

import chapi.domain.core.CodeDataStruct
import org.archguard.rule.core.Issue
import org.archguard.scanner.core.diffchanges.ChangedCall
import org.archguard.scanner.core.git.GitLogs
import org.archguard.scanner.core.sca.CompositionDependency
import org.archguard.scanner.core.sourcecode.CodeDatabaseRelation
import org.archguard.scanner.core.sourcecode.ContainerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/scanner/{systemId}/reporting")
class ScannerReportingController() {
    @Autowired
    private lateinit var service: ScannerReportingService

    @PostMapping(
        "/class-items",
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE, "application/x-protobuf"],
        consumes = [MediaType.APPLICATION_OCTET_STREAM_VALUE, "application/x-protobuf", "application/json"]
    )
    fun saveClassItems(
        @PathVariable systemId: String,
        @RequestParam language: String,
        @RequestParam path: String,
        @RequestBody input: List<CodeDataStruct>,
    ) {
        service.processClassItems(systemId, language, path, input)
    }

    @PostMapping("/container-services", consumes = ["application/x-protobuf", "application/json"])
    fun saveContainerServices(
        @PathVariable systemId: String,
        @RequestParam language: String,
        @RequestParam path: String,
        @RequestBody input: List<ContainerService>,
    ) {
        service.processContainerServices(systemId, language, path, input)
    }


    @PostMapping("/datamap-relations", consumes = ["application/x-protobuf", "application/json"])
    fun saveRelations(
        @PathVariable systemId: String,
        @RequestParam language: String,
        @RequestParam path: String,
        @RequestBody input: List<CodeDatabaseRelation>,
    ) {
        service.processDatamapRelations(systemId, language, path, input)
    }

    @PostMapping("/git-logs", consumes = ["application/x-protobuf", "application/json"])
    fun saveGitLogs(@PathVariable systemId: Long, @RequestBody inputs: List<GitLogs>) {
        service.processGitLogs(inputs, systemId)
    }

    @PostMapping("/diff-changes", consumes = ["application/x-protobuf", "application/json"])
    fun saveDiffs(
        @PathVariable systemId: Long,
        @RequestParam since: String,
        @RequestParam until: String,
        @RequestBody input: List<ChangedCall>,
    ) {
        service.processDiffChanges(systemId, since, until, input)
    }

    @PostMapping("/sca-dependencies", consumes = ["application/x-protobuf", "application/json"])
    fun saveDependencies(@PathVariable systemId: Long, @RequestBody input: List<CompositionDependency>) {
        service.processScaDependencies(systemId, input)
    }

    @PostMapping("/issues", consumes = ["application/x-protobuf", "application/json"])
    fun saveIssues(@PathVariable systemId: Long, @RequestBody input: List<Issue>) {
        service.processIssues(input, systemId)
    }
}
