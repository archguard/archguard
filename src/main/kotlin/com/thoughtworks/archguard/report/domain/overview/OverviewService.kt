package com.thoughtworks.archguard.report.domain.overview

import com.thoughtworks.archguard.report.domain.coupling.CouplingRepository
import com.thoughtworks.archguard.report.domain.dataclumps.DataClumpsRepository
import com.thoughtworks.archguard.report.domain.deepinheritance.DeepInheritanceRepository
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculateService
import com.thoughtworks.archguard.report.domain.overview.calculator.ModuleOverSizingCalculator
import com.thoughtworks.archguard.report.domain.sizing.SizingRepository
import com.thoughtworks.archguard.report.domain.sizing.SystemOverviewRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class OverviewService(val sizingRepository: SizingRepository,
                      val dataClumpsRepository: DataClumpsRepository,
                      val couplingRepository: CouplingRepository,
                      val systemOverviewRepository: SystemOverviewRepository,
                      val deepInheritanceRepository: DeepInheritanceRepository,
                      val badSmellCalculateService: BadSmellCalculateService) {


    @Autowired
    lateinit var moduleCalculator: ModuleOverSizingCalculator


    @Value("\${threshold.method.line}")
    private val methodSizingThreshold: Int = 0

    @Value("\${threshold.class.line}")
    private val classSizingThreshold: Int = 0

    @Value("\${threshold.class.method.count}")
    private val classMethodCountSizingThreshold: Int = 0


    @Value("\${threshold.package.line}")
    private val packageSizingLineThreshold: Int = 0

    @Value("\${threshold.package.class.count}")
    private val packageClassCountSizingThreshold: Int = 0

    @Value("\${threshold.module.line}")
    private val moduleSizingLineThreshold: Int = 0

    @Value("\${threshold.module.package.count}")
    private val modulePackageCountSizingThreshold: Int = 0

    @Value("\${threshold.class.fanIn}")
    private val classFanInThreshold: Int = 0

    @Value("\${threshold.class.fanOut}")
    private val classFanOutThreshold: Int = 0

    @Value("\${threshold.dataclumps.lcom4}")
    private val dataClumpsLCOM4Threshold: Int = 1

    @Value("\${threshold.deep-inheritance.dit}")
    private val deepInheritanceDitThreshold: Int = 1

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
        list.add(this.getClassHubOverview(systemId))
        list.add(this.getDataClumpsOverview(systemId))
        list.add(this.getDeepInheritanceOverview(systemId))
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

    private fun getClassHubOverview(systemId: Long): BadSmellOverviewItem {
        val count = couplingRepository.getCouplingAboveThresholdCount(systemId, classFanInThreshold, classFanOutThreshold)
        return BadSmellOverviewItem(BadSmell.COUPLING_CLASS_HUB, BadSmellCategory.COUPLING, BadSmellLevel.A, count)
    }

    private fun getDataClumpsOverview(systemId: Long): BadSmellOverviewItem {
        val count = dataClumpsRepository.getLCOM4AboveThresholdCount(systemId, dataClumpsLCOM4Threshold)
        return BadSmellOverviewItem(BadSmell.COUPLING_DATA_CLUMPS, BadSmellCategory.COUPLING, BadSmellLevel.A, count)
    }

    private fun getDeepInheritanceOverview(systemId: Long): BadSmellOverviewItem {
        val count = deepInheritanceRepository.getDitAboveThresholdCount(systemId, deepInheritanceDitThreshold)
        return BadSmellOverviewItem(BadSmell.COUPLING_DEEP_INHERITANCE, BadSmellCategory.COUPLING, BadSmellLevel.A, count)
    }

    private fun getCycleDependency(systemId: Long): BadSmellOverviewItem {
        //todo
        val count: Long = 0
        return BadSmellOverviewItem(BadSmell.COUPLING_CYCLE_DEPENDENCY, BadSmellCategory.COUPLING, BadSmellLevel.A, count)
    }


}
