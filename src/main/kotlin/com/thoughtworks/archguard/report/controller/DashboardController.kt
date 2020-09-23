package com.thoughtworks.archguard.report.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}")
class DashboardController {

    @GetMapping("/dashboard")
    fun getDashborad(@PathVariable("systemId") systemId: Long): ResponseEntity<List<Dashboard>> {
        val couplingDashboard = Dashboard(DashboardGroup.COUPLING,
                listOf(
                        GroupData(BadSmellType.DATACLUMPS, BadSmellLevel.A,
                                listOf(GraphData("2020-10-1", 4),
                                        GraphData("2020-10-5", 10),
                                        GraphData("2020-10-12", 5),
                                        GraphData("2020-10-15", 8),
                                        GraphData("2020-10-16", 20))),
                        GroupData(BadSmellType.DEEPINHERITANCE, BadSmellLevel.B,
                                listOf(GraphData("2020-10-1", 3),
                                        GraphData("2020-10-5", 4),
                                        GraphData("2020-10-12", 5),
                                        GraphData("2020-10-15", 5),
                                        GraphData("2020-10-16", 9)))
                ))
        val sizingDashboard = Dashboard(DashboardGroup.SIZING,
                listOf(
                        GroupData(BadSmellType.SIZINGMODULES, BadSmellLevel.C,
                                listOf(GraphData("2020-10-1", 4),
                                        GraphData("2020-10-5", 3),
                                        GraphData("2020-10-12", 9),
                                        GraphData("2020-10-15", 8),
                                        GraphData("2020-10-16", 20))),
                        GroupData(BadSmellType.SIZINGPACKAGE, BadSmellLevel.A,
                                listOf(GraphData("2020-10-1", 3),
                                        GraphData("2020-10-5", 4),
                                        GraphData("2020-10-12", 15),
                                        GraphData("2020-10-15", 20),
                                        GraphData("2020-10-16", 9))),
                        GroupData(BadSmellType.SIZINGCLASS, BadSmellLevel.D,
                                listOf(GraphData("2020-10-1", 303),
                                        GraphData("2020-10-5", 34),
                                        GraphData("2020-10-12", 15),
                                        GraphData("2020-10-15", 95),
                                        GraphData("2020-10-16", 39))),
                        GroupData(BadSmellType.SIZINGMETHOD, BadSmellLevel.B,
                                listOf(GraphData("2020-10-1", 3),
                                        GraphData("2020-10-5", 334),
                                        GraphData("2020-10-12", 235),
                                        GraphData("2020-10-15", 35),
                                        GraphData("2020-10-16", 129)))
                ))
        return ResponseEntity.ok(listOf(couplingDashboard, sizingDashboard))
    }
}

enum class DashboardGroup(var value: String) {
    COUPLING("过高耦合"),
    SIZING("体量过大")
}

enum class BadSmellLevel {
    A, B, C, D
}

enum class BadSmellType(var value: String) {
    DATACLUMPS("数据泥团"),
    DEEPINHERITANCE("过深继承"),
    CLASSHUB("枢纽类"),
    METHODHUB("枢纽方法"),
    PACKAGEHUB("枢纽包"),
    MODULEHUB("枢纽模块"),
    CYCLEDEPENDENCY("循环依赖"),
    SIZINGMODULES("子模块过大"),
    SIZINGPACKAGE("包过大"),
    SIZINGMETHOD("方法过大"),
    SIZINGCLASS("类过大")
}

class Dashboard(eDashboardGroup: DashboardGroup, val groupData: List<GroupData>) {
    var dashboardGroup: String = eDashboardGroup.value
}

class GroupData(eBadSmellType: BadSmellType, val level: BadSmellLevel, val graphData: List<GraphData>) {
    var type: String = eBadSmellType.value
}

data class GraphData(val date: String, val value: Int)
