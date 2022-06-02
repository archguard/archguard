package com.thoughtworks.archguard.evolution.controller

import com.thoughtworks.archguard.evolution.domain.IssueModel
import com.thoughtworks.archguard.evolution.domain.IssueService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/systems/")
class IssueController(val issueServices: IssueService) {

    @GetMapping("/{systemId}/issue")
    fun getAllIssue(@PathVariable("systemId") systemId: Long): List<IssueModel> {
        return issueServices.getAllIssues(systemId)
    }
}
