package com.thoughtworks.archguard.evolution.controller

import com.thoughtworks.archguard.evolution.domain.IssueModel
import com.thoughtworks.archguard.evolution.domain.IssueService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/issue")
class IssueController(val issueServices: IssueService) {

    @GetMapping("/{id}")
    fun getAllIssue(@PathVariable("id") id: Long): List<IssueModel> {
        return issueServices.getAllIssues(id)
    }
}
