package com.thoughtworks.archguard.report.application

import com.thoughtworks.archguard.metrics.infrastructure.influx.InfluxDBClient
import com.thoughtworks.archguard.report.controller.GroupData
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellLevel
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import com.thoughtworks.archguard.report.domain.badsmell.DashboardGroup
import com.thoughtworks.archguard.report.domain.circulardependency.CircularDependencyService
import com.thoughtworks.archguard.report.domain.cohesion.DataClassService
import com.thoughtworks.archguard.report.domain.cohesion.ShotgunSurgeryService
import com.thoughtworks.archguard.report.domain.dataclumps.DataClumpsService
import com.thoughtworks.archguard.report.domain.deepinheritance.DeepInheritanceService
import com.thoughtworks.archguard.report.domain.hub.HubService
import com.thoughtworks.archguard.report.domain.redundancy.OverGeneralizationService
import com.thoughtworks.archguard.report.domain.redundancy.RedundancyService
import com.thoughtworks.archguard.report.domain.sizing.SizingService
import org.springframework.stereotype.Service
import kotlin.math.roundToInt

@Service
class DashboardService(val sizingService: SizingService,
                       val hubService: HubService,
                       val dataClumpsService: DataClumpsService,
                       val deepInheritanceService: DeepInheritanceService,
                       val circularDependencyService: CircularDependencyService,
                       val redundancyService: RedundancyService,
                       val overGeneralizationService: OverGeneralizationService,
                       val dataClassService: DataClassService,
                       val shotgunSurgeryService: ShotgunSurgeryService,
                       val influxDBClient: InfluxDBClient) {

    private val TIME: String = "1d";
    private val SIZNG_REPORT: String = "sizing_report"
    private val COUPLING_REPORT: String = "coupling_report"
    private val REDUNDANCY_REPORT: String = "redundancy_report"
    private val COHESION_REPORT: String = "cohesion_report"

    fun getDashboard(systemId: Long): List<Dashboard> {
        val couplingDashboard = getDashboard(systemId, DashboardGroup.COUPLING, COUPLING_REPORT)
        val sizingDashboard = getDashboard(systemId, DashboardGroup.SIZING, SIZNG_REPORT)
        val redundancyDashboard = getDashboard(systemId, DashboardGroup.REDUNDANCY, REDUNDANCY_REPORT)
        val cohesionDashboard = getDashboard(systemId, DashboardGroup.COHESION, COHESION_REPORT)
        val testDashboard = getDashboard(systemId, DashboardGroup.TESTBADSMELL, TEST_BAD_SMELL_REPORT)
        return listOf(couplingDashboard, sizingDashboard, redundancyDashboard, cohesionDashboard, testDashboard)
    }

    fun saveReport(systemId: Long) {
        val sizingReport = sizingService.getSizingReport(systemId)
        influxDBClient.saveReport(SIZNG_REPORT, systemId.toString(), sizingReport)

        val hubReport = hubService.getHubReport(systemId)
        val dataClumpsReport = dataClumpsService.getDataClumpReport(systemId)
        val deepInheritanceReport = deepInheritanceService.getDeepInheritanceReport(systemId)
        val circularDependencyReport = circularDependencyService.getCircularDependencyReport(systemId)
        influxDBClient.saveReport(COUPLING_REPORT, systemId.toString(),
                hubReport.plus(dataClumpsReport).plus(deepInheritanceReport).plus(circularDependencyReport))

        val redundancyElementReport = redundancyService.getRedundantReport(systemId)
        val overGeneralizationReport = overGeneralizationService.getRedundantReport(systemId)
        influxDBClient.saveReport(REDUNDANCY_REPORT, systemId.toString(),
                redundancyElementReport.plus(overGeneralizationReport))

        val dataClassReport = dataClassService.getCohesionReport(systemId)
        val shotgunSurgeryReport = shotgunSurgeryService.getCohesionReport(systemId)
        influxDBClient.saveReport(COHESION_REPORT, systemId.toString(),
                dataClassReport.plus(shotgunSurgeryReport))
    }


    private fun queryReport(systemId: Long, badSmellType: BadSmellType, reportName: String): List<GraphData> {
        val query = "SELECT " +
                "mean(\"${badSmellType.name}\") AS \"${badSmellType.name}\" " +
                "FROM \"${reportName}\" " +
                "WHERE (\"system_id\" = '${systemId}') GROUP BY time(${TIME}) fill(none)"
        return influxDBClient.query(query).map { it.values }
                .flatten().map { GraphData(it[0], it[1].toDouble().roundToInt()) }
    }

    private fun getDashboard(systemId: Long, dashboardGroup: DashboardGroup, reportName: String): Dashboard {
        val groupData = dashboardGroup.badSmells
                .map {
                    GroupData(it, it.calculate(systemId)?.level
                            ?: BadSmellLevel.A, queryReport(systemId, it, reportName))
                }
        return Dashboard(dashboardGroup, groupData)
    }
}

class Dashboard(eDashboardGroup: DashboardGroup, val groupData: List<GroupData>) {
    var dashboardGroup: String = eDashboardGroup.value
}

enum class DashboardGroup(val value: String, val badSmells: List<BadSmellType>) {
    COUPLING("过高耦合", listOf(
            BadSmellType.DATACLUMPS,
            BadSmellType.DEEPINHERITANCE,
            BadSmellType.CLASSHUB,
            BadSmellType.METHODHUB,
            BadSmellType.PACKAGEHUB,
            BadSmellType.MODULEHUB,
            BadSmellType.CYCLEDEPENDENCY)),
    SIZING("体量过大", listOf(
            BadSmellType.SIZINGMODULES,
            BadSmellType.SIZINGPACKAGE,
            BadSmellType.SIZINGMETHOD,
            BadSmellType.SIZINGCLASS)),
    COHESION("内聚度不足", listOf(
            BadSmellType.DATA_CLASS,
            BadSmellType.SHOTGUN_SURGERY)),
    REDUNDANCY("冗余度高", listOf(
            BadSmellType.REDUNDANT_ELEMENT,
            BadSmellType.OVER_GENERALIZATION)),
    TESTBADSMELL("测试薄弱", listOf(
            BadSmellType.REDUNDANT_ELEMENT,
            BadSmellType.OVER_GENERALIZATION)),
    UNDEFINED("未找到", listOf());

    companion object {
        fun getGroup(badSmellType: BadSmellType): DashboardGroup {
            return values().find { it.badSmells.contains(badSmellType) } ?: UNDEFINED
        }
    }
}

data class GraphData(val date: String, val value: Int)

