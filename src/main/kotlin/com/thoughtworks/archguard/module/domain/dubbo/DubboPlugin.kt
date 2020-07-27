package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.clazz.domain.ClazzType
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.getModule
import com.thoughtworks.archguard.module.domain.model.*
import com.thoughtworks.archguard.module.domain.plugin.Plugin
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("DubboPlugin")
class DubboPlugin : Plugin() {
    private val log = LoggerFactory.getLogger(DubboPlugin::class.java)

    @Autowired
    lateinit var xmlConfigService: XmlConfigService

    @Autowired
    lateinit var jClassRepository: JClassRepository

    override fun mapToModuleDependencies(methodDependencies: List<Dependency<JMethodVO>>, logicModules: List<LogicModule>, logicModuleDependencies: List<Dependency<LogicModule>>): List<Dependency<LogicModule>> {
        val interfaces = jClassRepository.getJClassesHasModules().filter { it.classType == ClazzType.INTERFACE }.map { "${it.module}.${it.name}" }
        // calleeClass不是接口类型，直接停止分析
        val interfaceDependencies =  methodDependencies.filter { "${it.callee.jClassVO.module}.${it.callee.jClassVO.name}" in interfaces }
        return logicModuleDependencies + interfaceDependencies.flatMap { mapToModuleDependency(it, logicModules) }

    }

    private fun mapToModuleDependency(methodDependency: Dependency<JMethodVO>, logicModules: List<LogicModule>): List<Dependency<LogicModule>> {
        val callerClass = methodDependency.caller.jClassVO
        val calleeClass = methodDependency.callee.jClassVO

        val dubboAnalysisCalleeModules = analysis(Dependency(callerClass, calleeClass), logicModules)

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
