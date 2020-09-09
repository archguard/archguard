package com.thoughtworks.archguard.report.domain.sizing

interface SystemOverviewRepository {
    fun getSystemInfoRepoBySystemId(systemId: Long): String
    fun getSystemModuleCountBySystemId(systemId: Long): Long
    fun getSystemLineCountBySystemId(systemId: Long): Long
}
