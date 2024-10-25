package com.thoughtworks.archguard.smartscanner.controller

import chapi.domain.core.CodeDataStruct
import org.archguard.rule.core.Issue
import org.archguard.model.ChangedCall
import org.archguard.model.LanguageEstimate
import org.archguard.model.GitLogs
import org.archguard.model.CompositionDependency
import org.archguard.model.CodeDatabaseRelation
import org.archguard.model.ContainerService
import org.springframework.beans.factory.annotation.Autowired
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


    @PostMapping("/class-items")
    fun saveClassItems(
        @PathVariable systemId: String,
        @RequestParam language: String,
        @RequestParam path: String,
        @RequestBody input: List<CodeDataStruct>,
    ) {
        service.processClassItems(systemId, language, path, input)
    }

    @PostMapping("/container-services")
    fun saveContainerServices(
        @PathVariable systemId: String,
        @RequestParam language: String,
        @RequestParam path: String,
        @RequestBody input: List<ContainerService>,
    ) {
        service.processContainerServices(systemId, language, path, input)
    }


    @PostMapping("/datamap-relations")
    fun saveRelations(
        @PathVariable systemId: String,
        @RequestParam language: String,
        @RequestParam path: String,
        @RequestBody input: List<CodeDatabaseRelation>,
    ) {
        service.processDatamapRelations(systemId, language, path, input)
    }

    @PostMapping("/git-logs")
    fun saveGitLogs(@PathVariable systemId: Long, @RequestBody inputs: List<GitLogs>) {
        service.processGitLogs(inputs, systemId)
    }

    @PostMapping("/diff-changes")
    fun saveDiffs(
        @PathVariable systemId: Long,
        @RequestParam since: String,
        @RequestParam until: String,
        @RequestBody input: List<ChangedCall>,
    ) {
        service.processDiffChanges(systemId, since, until, input)
    }

    @PostMapping("/sca-dependencies")
    fun saveDependencies(@PathVariable systemId: Long, @RequestBody input: List<CompositionDependency>) {
        service.processScaDependencies(systemId, input)
    }

    @PostMapping("/issues")
    fun saveIssues(@PathVariable systemId: Long, @RequestBody input: List<Issue>) {
        service.processIssues(input, systemId)
    }

    @PostMapping("/estimates")
    fun saveEstimates(@PathVariable systemId: Long, @RequestBody input: List<LanguageEstimate>) {
        service.processEstimates(input, systemId)
    }
}
