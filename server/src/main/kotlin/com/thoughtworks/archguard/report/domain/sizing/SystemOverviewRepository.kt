package com.thoughtworks.archguard.report.domain.sizing

import com.thoughtworks.archguard.report.domain.overview.SystemLanguage

interface SystemOverviewRepository {
    fun getSystemInfoRepoBySystemId(systemId: Long): String
    fun getSystemModuleCountBySystemId(systemId: Long): Long
    fun getSystemLineCountBySystemId(systemId: Long): Long
    fun getContributorCountBySystemId(systemId: Long): Long
    fun getLineCountBySystemIdWithLanguage(systemId: Long): List<SystemLanguage>
}
