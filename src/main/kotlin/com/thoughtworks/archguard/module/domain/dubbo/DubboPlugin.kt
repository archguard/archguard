package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.module.domain.getModule
import com.thoughtworks.archguard.module.domain.model.*
import com.thoughtworks.archguard.module.domain.plugin.Plugin
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DubboPlugin : Plugin() {
    private val log = LoggerFactory.getLogger(DubboPlugin::class.java)

    @Autowired
    lateinit var xmlConfigService: XmlConfigService

    override fun mapToModuleDependency(methodDependency: Dependency<JMethodVO>, logicModules: List<LogicModule>, logicModuleDependencies: List<Dependency<LogicModule>>): List<Dependency<LogicModule>> {
        val callerClass = methodDependency.caller.jClassVO
        val calleeClass = methodDependency.callee.jClassVO

        // TODO 查询classType信息
        // calleeClass不是接口类型，直接停止分析
        if (!calleeClass.isInterface()) {
            return logicModuleDependencies
        }

        val dubboAnalysisCalleeModules = analysis(Dependency(callerClass, calleeClass), logicModules)
        if (dubboAnalysisCalleeModules.isEmpty()) {
            return logicModuleDependencies
        }

        val callerModules = getModule(logicModules, callerClass)
        val calleeModules = getModule(logicModules, calleeClass)

        val calleeModulesAfterAnalysis = calleeModules.intersect(dubboAnalysisCalleeModules)
        return callerModules.flatMap { caller -> calleeModulesAfterAnalysis.map { callee -> Dependency(caller, callee) } }
    }

    private fun analysis(classDependency: Dependency<JClassVO>, logicModules: List<LogicModule>): List<LogicModule> {
        val calleeSubModuleByXml = xmlConfigService.getRealCalleeModuleByXmlConfig(classDependency.caller, classDependency.callee)
        return calleeSubModuleByXml.map { getModule(logicModules, SubModule(it.name)) }
                .flatten().toSet().toList()
    }
}
