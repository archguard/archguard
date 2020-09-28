package com.thoughtworks.archguard.report.application

import com.thoughtworks.archguard.metrics.infrastructure.influx.InfluxDBClient
import com.thoughtworks.archguard.report.controller.GroupData
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import com.thoughtworks.archguard.report.domain.circulardependency.CircularDependencyService
import com.thoughtworks.archguard.report.domain.dataclumps.DataClumpsService
import com.thoughtworks.archguard.report.domain.deepinheritance.DeepInheritanceService
import com.thoughtworks.archguard.report.domain.hub.HubService
import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellCalculator
import com.thoughtworks.archguard.report.domain.sizing.SizingService
import org.springframework.stereotype.Service
import kotlin.math.roundToInt

@Service
class DashboardService(val badSmellCalculator: BadSmellCalculator,
                       val sizingService: SizingService,
                       val hubService: HubService,
                       val dataClumpsService: DataClumpsService,
                       val deepInheritanceService: DeepInheritanceService,
                       val circularDependencyService: CircularDependencyService,
                       val influxDBClient: InfluxDBClient) {

    private val TIME: String = "1m";
    private val SIZNG_REPORT: String = "sizing_report"
    private val COUPLING_REPORT: String = "coupling_report"

    fun getDashboard(systemId: Long): List<Dashboard> {
        val couplingDashboard = getCouplingDashboard(systemId)
        val sizingDashboard = getSizingDashboard(systemId)
        return listOf(couplingDashboard, sizingDashboard)
    }

    private fun getSizingDashboard(systemId: Long): Dashboard {

        return Dashboard(DashboardGroup.SIZING,
                listOf(
                        GroupData(BadSmellType.SIZINGMODULES,
                                badSmellCalculator.calculateBadSmell(BadSmellType.SIZINGMODULES, systemId).level,
                                queryReport(systemId, BadSmellType.SIZINGMODULES, SIZNG_REPORT)),
                        GroupData(BadSmellType.SIZINGPACKAGE,
                                badSmellCalculator.calculateBadSmell(BadSmellType.SIZINGPACKAGE, systemId).level,
                                queryReport(systemId, BadSmellType.SIZINGPACKAGE, SIZNG_REPORT)),
                        GroupData(BadSmellType.SIZINGCLASS,
                                badSmellCalculator.calculateBadSmell(BadSmellType.SIZINGCLASS, systemId).level,
                                queryReport(systemId, BadSmellType.SIZINGCLASS, SIZNG_REPORT)),
                        GroupData(BadSmellType.SIZINGMETHOD,
                                badSmellCalculator.calculateBadSmell(BadSmellType.SIZINGMETHOD, systemId).level,
                                queryReport(systemId, BadSmellType.SIZINGMETHOD, SIZNG_REPORT))
                )
        )
    }

    private fun queryReport(systemId: Long, badSmellType: BadSmellType, reportName: String): List<GraphData> {
        val query = "SELECT " +
                "mean(\"${badSmellType.name}\") AS \"${badSmellType.name}\" " +
                "FROM \"${reportName}\" " +
                "WHERE (\"system_id\" = '${systemId}') GROUP BY time(${TIME}) fill(none)"
        return influxDBClient.query(query).map { it.values }
                .flatten().map { GraphData(it[0], it[1].toDouble().roundToInt()) }
    }

    private fun getCouplingDashboard(systemId: Long): Dashboard {
        return Dashboard(DashboardGroup.COUPLING,
                listOf(
                        GroupData(BadSmellType.DATACLUMPS,
                                badSmellCalculator.calculateBadSmell(BadSmellType.DATACLUMPS, systemId).level,
                                queryReport(systemId, BadSmellType.DATACLUMPS, COUPLING_REPORT)),
                        GroupData(BadSmellType.DEEPINHERITANCE,
                                badSmellCalculator.calculateBadSmell(BadSmellType.DEEPINHERITANCE, systemId).level,
                                queryReport(systemId, BadSmellType.DEEPINHERITANCE, COUPLING_REPORT)),
                        GroupData(BadSmellType.CLASSHUB,
                                badSmellCalculator.calculateBadSmell(BadSmellType.CLASSHUB, systemId).level,
                                queryReport(systemId, BadSmellType.CLASSHUB, COUPLING_REPORT)),
                        GroupData(BadSmellType.METHODHUB,
                                badSmellCalculator.calculateBadSmell(BadSmellType.METHODHUB, systemId).level,
                                queryReport(systemId, BadSmellType.METHODHUB, COUPLING_REPORT)),
                        GroupData(BadSmellType.PACKAGEHUB,
                                badSmellCalculator.calculateBadSmell(BadSmellType.PACKAGEHUB, systemId).level,
                                queryReport(systemId, BadSmellType.PACKAGEHUB, COUPLING_REPORT)),
                        GroupData(BadSmellType.MODULEHUB,
                                badSmellCalculator.calculateBadSmell(BadSmellType.MODULEHUB, systemId).level,
                                queryReport(systemId, BadSmellType.MODULEHUB, COUPLING_REPORT)),
                        GroupData(BadSmellType.CYCLEDEPENDENCY,
                                badSmellCalculator.calculateBadSmell(BadSmellType.CYCLEDEPENDENCY, systemId).level,
                                queryReport(systemId, BadSmellType.CYCLEDEPENDENCY, COUPLING_REPORT))
                )
        )
    }

    fun saveReport(systemId: Long) {
        val sizingReport = sizingService.getSizingReport(systemId).map { "${it.key}=${it.value}" }.joinToString(",")
        influxDBClient.save("sizing_report,system_id=${systemId} ${sizingReport}")

        val hubReport = hubService.getHubReport(systemId).map { "${it.key}=${it.value}" }.joinToString(",")
        val dataClumpsReport = dataClumpsService.getDataClumpReport(systemId).map { "${it.key}=${it.value}" }.joinToString(",")
        val deepInheritanceReport = deepInheritanceService.getDeepInheritanceReport(systemId).map { "${it.key}=${it.value}" }.joinToString(",")
        val circularDependencyReport = circularDependencyService.getCircularDependencyReport(systemId).map { "${it.key}=${it.value}" }.joinToString(",")
        influxDBClient.save("coupling_report,system_id=${systemId} " +
                "${hubReport},${dataClumpsReport},${deepInheritanceReport},${circularDependencyReport}")
    }


}

class Dashboard(eDashboardGroup: DashboardGroup, val groupData: List<GroupData>) {
    var dashboardGroup: String = eDashboardGroup.value
}

enum class DashboardGroup(var value: String) {
    COUPLING("过高耦合"),
    SIZING("体量过大"),
    COHESION("内聚度不足"),
    REDUNDANCY("冗余度高")
}

data class GraphData(val date: String, val value: Int)

