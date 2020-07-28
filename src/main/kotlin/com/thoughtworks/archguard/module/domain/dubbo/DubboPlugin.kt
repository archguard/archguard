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
    lateinit var dubboXmlDependencyAnalysisHelper: DubboXmlDependencyAnalysisHelper

    @Autowired
    lateinit var jClassRepository: JClassRepository

    override fun mapToModuleDependencies(methodDependencies: List<Dependency<JClassVO>>, logicModules: List<LogicModule>, logicModuleDependencies: List<Dependency<LogicModule>>): List<Dependency<LogicModule>> {
        val interfaces = jClassRepository.getJClassesHasModules().filter { it.classType == ClazzType.INTERFACE }.map { "${it.module}.${it.name}" }
        return methodDependencies.flatMap { mapToModuleDependency(it, logicModules, interfaces) }

    }

    private fun mapToModuleDependency(methodDependency: Dependency<JClassVO>, logicModules: List<LogicModule>, interfaces: List<String>): List<Dependency<LogicModule>> {
        val callerClass = methodDependency.caller
        val calleeClass = methodDependency.callee

        val callerModules = getModule(logicModules, callerClass)
        val calleeModules = getModule(logicModules, calleeClass)

        // calleeClass不是接口类型，直接停止分析
        if (!isInterface(calleeClass, interfaces)) {
            return callerModules.flatMap { caller -> calleeModules.map { callee -> Dependency(caller, callee) } }
        }

        val dubboAnalysisCalleeModules = dubboXmlDependencyAnalysisHelper.analysis(Dependency(callerClass, calleeClass), logicModules)
        val calleeModulesAfterAnalysis = calleeModules.intersect(dubboAnalysisCalleeModules)
        return callerModules.flatMap { caller -> calleeModulesAfterAnalysis.map { callee -> Dependency(caller, callee) } }
    }

    private fun isInterface(jClassVO: JClassVO, interfaces: List<String>): Boolean{
        return interfaces.contains("${jClassVO.module}.${jClassVO.name}")
    }
}
