package com.thoughtworks.archguard.report.domain.overview

import com.thoughtworks.archguard.report.controller.BadSmellType
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculator
import com.thoughtworks.archguard.report.domain.sizing.SystemOverviewRepository
import org.springframework.stereotype.Service

@Service
class OverviewService(val systemOverviewRepository: SystemOverviewRepository,
                      val badSmellCalculator: BadSmellCalculator) {

    fun getSystemOverview(systemId: Long): SystemOverview {
        val repoCount = getRepoCount(systemId)
        val moduleCount = getModuleCount(systemId)
        val lineCount = getLineCount(systemId)
        val contributorCount = getContributorCount(systemId)
        return SystemOverview(repoCount, moduleCount, lineCount, contributorCount)
    }

    private fun getContributorCount(systemId: Long): Long {
        return systemOverviewRepository.getContributorCountBySystemId(systemId)
    }

    fun getOverview(systemId: Long): BadSmellOverviewDto {
        val list = mutableListOf<BadSmellOverviewItem>()
        list.add(badSmellCalculator.calculateBadSmell(BadSmellType.SIZINGMETHOD, systemId))
        list.add(badSmellCalculator.calculateBadSmell(BadSmellType.SIZINGCLASS, systemId))
        list.add(badSmellCalculator.calculateBadSmell(BadSmellType.SIZINGPACKAGE, systemId))
        list.add(badSmellCalculator.calculateBadSmell(BadSmellType.SIZINGMODULES, systemId))
        list.add(badSmellCalculator.calculateBadSmell(BadSmellType.CLASSHUB, systemId))
        list.add(badSmellCalculator.calculateBadSmell(BadSmellType.METHODHUB, systemId))
        list.add(badSmellCalculator.calculateBadSmell(BadSmellType.MODULEHUB, systemId))
        list.add(badSmellCalculator.calculateBadSmell(BadSmellType.PACKAGEHUB, systemId))
        list.add(badSmellCalculator.calculateBadSmell(BadSmellType.DATACLUMPS, systemId))
        list.add(badSmellCalculator.calculateBadSmell(BadSmellType.DEEPINHERITANCE, systemId))
        list.add(badSmellCalculator.calculateBadSmell(BadSmellType.CYCLEDEPENDENCY, systemId))
        // todo 补充其他维度坏味道
        return BadSmellOverviewDto(list)
    }

    private fun getRepoCount(systemId: Long): Int {
        val repo = systemOverviewRepository.getSystemInfoRepoBySystemId(systemId)
        val repoList: List<String> = repo.split(",")
        return repoList.size
    }

    private fun getModuleCount(systemId: Long): Long {
        return systemOverviewRepository.getSystemModuleCountBySystemId(systemId)
    }

    private fun getLineCount(systemId: Long): Long {
        return systemOverviewRepository.getSystemLineCountBySystemId(systemId)
    }
}
