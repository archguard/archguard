package com.thoughtworks.archguard.evolution.domain

import org.springframework.stereotype.Service

@Service
class IssueService(val evoIssueRepository: EvoIssueRepository) {
    fun getAllIssues(id: Long): List<IssueModel> {
        return evoIssueRepository.getAll(id)
    }
}
