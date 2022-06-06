package com.thoughtworks.archguard.issue

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/systems/")
class EvoIssueController(val evoIssueServices: EvoIssueService) {

    @GetMapping("/{systemId}/issue")
    fun getAllIssue(@PathVariable("systemId") systemId: Long): List<EvoIssueModel> {
        return evoIssueServices.getAllIssues(systemId)
    }
}
