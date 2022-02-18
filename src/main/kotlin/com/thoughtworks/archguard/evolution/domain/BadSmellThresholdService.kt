package com.thoughtworks.archguard.evolution.domain

import org.springframework.stereotype.Service

@Service
class BadSmellThresholdService(val badSmellSuitRepository: BadSmellSuiteRepository) {

    fun getAllSuits(): List<BadSmellSuite> {
        return badSmellSuitRepository.getAllBadSmellThresholdSuites()
    }

    fun getBadSmellSuiteWithSelectedInfoBySystemId(systemId: Long): List<BadSmellSuiteWithSelected> {
        val selectedId = badSmellSuitRepository.getSelectedBadSmellSuiteIdBySystem(systemId)
        return badSmellSuitRepository.getAllBadSmellThresholdSuites()
                .map {
                    BadSmellSuiteWithSelected(it.id, it.suiteName, it.isDefault, it.id == selectedId, it.thresholds)
                }
    }
}