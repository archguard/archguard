package com.thoughtworks.archguard.report.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Deprecated("Migrated to code-addition ")
@RestController
@RequestMapping("/systems/{systemId}/")
class BadSmellController() {

    @GetMapping("badsmell-thresholds")
    @Deprecated("Migrated to code-addition /evolution/badsmell-thresholds/system/4")
    fun getThresholds(@PathVariable("systemId") systemId: Long): List<OldBadSmellSuite> {
        return listOf(OldBadSmellSuite("id", "架构评估一级指标", true, listOf(
                OldBadSmellGroup("体量维度", listOf(
                        OldBadSmellThreshold("过大的方法", "方法代码行数 > 指标阈值", 30),
                        OldBadSmellThreshold("过大的类", "方法个数 > 指标阈值", 20),
                        OldBadSmellThreshold("过大的类", "方法代码行数 > 指标阈值", 50)
                )),
                OldBadSmellGroup("耦合纬度", listOf(
                        OldBadSmellThreshold("枢纽模块", "出向依赖或入向依赖 > 指标阈值", 8),
                        OldBadSmellThreshold("枢纽包", "出向依赖或入向依赖  > 指标阈值", 8),
                        OldBadSmellThreshold("枢纽类", "出向依赖或入向依赖 > 指标阈值", 8)
                ))
        )), OldBadSmellSuite("id1", "架构评估二级指标", false, listOf(
                OldBadSmellGroup("体量维度", listOf(
                        OldBadSmellThreshold("过大的方法", "方法代码行数 > 指标阈值", 30),
                        OldBadSmellThreshold("过大的类", "方法个数 > 指标阈值", 20),
                        OldBadSmellThreshold("过大的类", "方法代码行数 > 指标阈值", 50)
                )),
                OldBadSmellGroup("耦合纬度", listOf(
                        OldBadSmellThreshold("枢纽模块", "出向依赖或入向依赖 > 指标阈值", 8),
                        OldBadSmellThreshold("枢纽包", "出向依赖或入向依赖  > 指标阈值", 8),
                        OldBadSmellThreshold("枢纽类", "出向依赖或入向依赖 > 指标阈值", 8)
                ))
        )))
    }
}

data class OldBadSmellSuite(val id: String, val title: String, val selected: Boolean, val thresholds: List<OldBadSmellGroup>)

data class OldBadSmellGroup(val name: String, val threshold: List<OldBadSmellThreshold>)

data class OldBadSmellThreshold(val name: String, val condition: String, val value: Int)


