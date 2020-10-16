package com.thoughtworks.archguard.report.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/")
class BadSmellController {

    @GetMapping("badsmell-thresholds")
    fun getThresholds(@PathVariable("systemId") systemId: Long): List<BadSmellSuite> {
        return listOf(BadSmellSuite("id", "架构评估一级指标", true, listOf(
                BadSmellGroup("体量维度", listOf(
                        BadSmellThreshold("过大的方法", "方法代码行数 > 指标阈值", 30),
                        BadSmellThreshold("过大的类", "方法个数 > 指标阈值", 20),
                        BadSmellThreshold("过大的类", "方法代码行数 > 指标阈值", 50)
                )),
                BadSmellGroup("耦合纬度", listOf(
                        BadSmellThreshold("枢纽模块", "出向依赖或入向依赖 > 指标阈值", 8),
                        BadSmellThreshold("枢纽包", "出向依赖或入向依赖  > 指标阈值", 8),
                        BadSmellThreshold("枢纽类", "出向依赖或入向依赖 > 指标阈值", 8)
                ))
        )))
    }
}

data class BadSmellSuite(val id: String, val title: String, val selected: Boolean, val thresholds: List<BadSmellGroup>)

data class BadSmellGroup(val name: String, val threshold: List<BadSmellThreshold>)

data class BadSmellThreshold(val name: String, val condition: String, val value: Int)


