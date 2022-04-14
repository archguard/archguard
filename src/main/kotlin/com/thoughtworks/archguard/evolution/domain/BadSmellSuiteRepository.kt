package com.thoughtworks.archguard.evolution.domain

interface BadSmellSuiteRepository {
    fun getAllBadSmellThresholdSuites(): List<BadSmellSuite>
    fun getSelectedBadSmellSuiteIdBySystem(systemId: Long): Long
}
