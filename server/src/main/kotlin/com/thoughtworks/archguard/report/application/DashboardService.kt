package com.thoughtworks.archguard.report.application

import com.thoughtworks.archguard.metrics.infrastructure.influx.InfluxDBClient
import com.thoughtworks.archguard.report.domain.GroupData
import org.archguard.smell.BadSmellLevelType
import org.archguard.smell.BadSmellType
import org.archguard.smell.BadSmellGroup
import com.thoughtworks.archguard.report.domain.cohesion.DataClassService
import com.thoughtworks.archguard.report.domain.cohesion.ShotgunSurgeryService
import com.thoughtworks.archguard.report.domain.coupling.circulardependency.CircularDependencyService
import com.thoughtworks.archguard.report.domain.coupling.dataclumps.DataClumpsService
import com.thoughtworks.archguard.report.domain.coupling.deepinheritance.DeepInheritanceService
import com.thoughtworks.archguard.report.domain.coupling.hub.HubService
import com.thoughtworks.archguard.report.domain.redundancy.OverGeneralizationService
import com.thoughtworks.archguard.report.domain.redundancy.RedundancyService
import com.thoughtworks.archguard.report.domain.sizing.SizingService
import com.thoughtworks.archguard.report.domain.testing.TestBadSmellService
import org.springframework.stereotype.Service
import kotlin.math.roundToInt

@Service
class DashboardService(
    val sizingService: SizingService,
    val hubService: HubService,
    val dataClumpsService: DataClumpsService,
    val deepInheritanceService: DeepInheritanceService,
    val circularDependencyService: CircularDependencyService,
    val redundancyService: RedundancyService,
    val overGeneralizationService: OverGeneralizationService,
    val dataClassService: DataClassService,
    val shotgunSurgeryService: ShotgunSurgeryService,
    val testBadSmellService: TestBadSmellService,
    val influxDBClient: InfluxDBClient
) {

    private val TIME: String = "1d"
    private val SIZNG_REPORT: String = "sizing_report"
    private val COUPLING_REPORT: String = "coupling_report"
    private val REDUNDANCY_REPORT: String = "redundancy_report"
    private val COHESION_REPORT: String = "cohesion_report"
    private val TEST_BAD_SMELL_REPORT: String = "test_report"

    fun getDashboard(systemId: Long): List<Dashboard> {
        val couplingDashboard = getDashboard(systemId, BadSmellGroup.COUPLING, COUPLING_REPORT)
        val sizingDashboard = getDashboard(systemId, BadSmellGroup.SIZING, SIZNG_REPORT)
        val redundancyDashboard = getDashboard(systemId, BadSmellGroup.REDUNDANCY, REDUNDANCY_REPORT)
        val cohesionDashboard = getDashboard(systemId, BadSmellGroup.COHESION, COHESION_REPORT)
        val testDashboard = getDashboard(systemId, BadSmellGroup.TESTBADSMELL, TEST_BAD_SMELL_REPORT)
        return listOf(couplingDashboard, sizingDashboard, redundancyDashboard, cohesionDashboard, testDashboard)
    }

    fun saveReport(systemId: Long) {
        val sizingReport = sizingService.getSizingReport(systemId)
        influxDBClient.saveReport(SIZNG_REPORT, systemId.toString(), sizingReport)

        val hubReport = hubService.getHubReport(systemId)
        val dataClumpsReport = dataClumpsService.getDataClumpReport(systemId)
        val deepInheritanceReport = deepInheritanceService.getDeepInheritanceReport(systemId)
        val circularDependencyReport = circularDependencyService.getCircularDependencyReport(systemId)
        influxDBClient.saveReport(
            COUPLING_REPORT, systemId.toString(),
            hubReport.plus(dataClumpsReport).plus(deepInheritanceReport).plus(circularDependencyReport)
        )

        val redundancyElementReport = redundancyService.getRedundantReport(systemId)
        val overGeneralizationReport = overGeneralizationService.getRedundantReport(systemId)
        influxDBClient.saveReport(
            REDUNDANCY_REPORT, systemId.toString(),
            redundancyElementReport.plus(overGeneralizationReport)
        )

        val dataClassReport = dataClassService.getCohesionReport(systemId)
        val shotgunSurgeryReport = shotgunSurgeryService.getCohesionReport(systemId)
        influxDBClient.saveReport(
            COHESION_REPORT, systemId.toString(),
            dataClassReport.plus(shotgunSurgeryReport)
        )

        val testReport = testBadSmellService.getTestingReport(systemId)
        influxDBClient.saveReport(TEST_BAD_SMELL_REPORT, systemId.toString(), testReport)
    }

    private fun queryReport(systemId: Long, badSmellType: BadSmellType, reportName: String): List<GraphData> {
        val query = "SELECT " +
            "mean(\"${badSmellType.name}\") AS \"${badSmellType.name}\" " +
            "FROM \"${reportName}\" " +
            "WHERE (\"system_id\" = '$systemId') GROUP BY time($TIME) fill(none)"
        return influxDBClient.query(query).map { it.values }
            .flatten().map {
                GraphData(
                    it[0],
                    it[1].toDouble().roundToInt()
                )
            }
    }

    private fun getDashboard(systemId: Long, badSmellGroup: BadSmellGroup, reportName: String): Dashboard {
        val groupData = badSmellGroup.badSmells
            .map {
                GroupData(
                    it,
                    it.badSmellCalculator?.getBadSmellOverviewItem(systemId, it)?.level
                        ?: BadSmellLevelType.A,
                    queryReport(systemId, it, reportName)
                )
            }
        return Dashboard(badSmellGroup, groupData)
    }
}

class Dashboard(eBadSmellGroup: BadSmellGroup, val groupData: List<GroupData>) {
    var dashboardGroup: String = eBadSmellGroup.value
}

data class GraphData(val date: String, val value: Int)
