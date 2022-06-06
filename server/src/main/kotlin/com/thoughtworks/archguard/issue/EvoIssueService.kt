package com.thoughtworks.archguard.issue

import org.springframework.stereotype.Service

@Service
class EvoIssueService(val evoIssueRepository: EvoIssueRepository) {
    fun getAllIssues(id: Long): List<EvoIssueModel> {
        return evoIssueRepository.getAll(id)
    }
}
