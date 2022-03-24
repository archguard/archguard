package com.thoughtworks.archguard.report.domain.overview

import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import com.thoughtworks.archguard.report.domain.sizing.SystemOverviewRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class OverviewService(val systemOverviewRepository: SystemOverviewRepository) {
    fun getSystemOverview(systemId: Long): SystemOverview {
        val repoCount = getRepoCount(systemId)
        val moduleCount = getModuleCount(systemId)
        val lineCount = getLineCountWithLanguage(systemId)
        val contributorCount = getContributorCount(systemId)
        return SystemOverview(repoCount, moduleCount, lineCount, contributorCount)
    }

    private fun getContributorCount(systemId: Long): Long {
        return systemOverviewRepository.getContributorCountBySystemId(systemId)
    }

    @Cacheable("systemOverview")
    fun getOverview(systemId: Long): BadSmellOverviewDto {
        return BadSmellOverviewDto(BadSmellType.values()
                .mapNotNull { it.calculate(systemId) })
    }

    private fun getRepoCount(systemId: Long): Int {
        val repo = systemOverviewRepository.getSystemInfoRepoBySystemId(systemId)
        val repoList: List<String> = repo.split(",")
        return repoList.size
    }

    private fun getModuleCount(systemId: Long): Long {
        return systemOverviewRepository.getSystemModuleCountBySystemId(systemId)
    }

    private fun getLineCountWithLanguage(systemId: Long): List<SystemLanguage> {
        return systemOverviewRepository.getLineCountBySystemIdWithLanguage(systemId)
    }
}
