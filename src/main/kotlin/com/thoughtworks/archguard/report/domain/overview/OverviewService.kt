package com.thoughtworks.archguard.report.domain.overview

import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateService
import com.thoughtworks.archguard.report.domain.sizing.SystemOverviewRepository
import org.springframework.stereotype.Service

@Service
class OverviewService(val systemOverviewRepository: SystemOverviewRepository,
                      val badSmellCalculateService: BadSmellCalculateService) {

    fun getSystemOverview(systemId: Long): SystemOverview {
        val repoCount = getRepoCount(systemId)
        val moduleCount = getModuleCount(systemId)
        val lineCount = getLineCount(systemId)
        return SystemOverview(repoCount, moduleCount, lineCount)
    }

    fun getOverview(systemId: Long): BadSmellOverviewDto {
        val list = mutableListOf<BadSmellOverviewItem>()
        list.add(badSmellCalculateService.calculateBadSmell(BadSmell.METHOD_OVER_SIZING, systemId))
        list.add(badSmellCalculateService.calculateBadSmell(BadSmell.CLASS_OVER_SIZING, systemId))
        list.add(badSmellCalculateService.calculateBadSmell(BadSmell.PACKAGE_OVER_SIZING, systemId))
        list.add(badSmellCalculateService.calculateBadSmell(BadSmell.MODULE_OVER_SIZING, systemId))
        list.add(badSmellCalculateService.calculateBadSmell(BadSmell.COUPLING_CLASS_HUB, systemId))
        list.add(badSmellCalculateService.calculateBadSmell(BadSmell.COUPLING_DATA_CLUMPS, systemId))
        list.add(badSmellCalculateService.calculateBadSmell(BadSmell.COUPLING_DEEP_INHERITANCE, systemId))
        list.add(this.getCycleDependency(systemId))
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

    private fun getCycleDependency(systemId: Long): BadSmellOverviewItem {
        //todo
        val count: Long = 0
        return BadSmellOverviewItem(BadSmell.COUPLING_CYCLE_DEPENDENCY, BadSmellCategory.COUPLING, BadSmellLevel.A, count)
    }


}
