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

    override fun mapToModuleDependencies(dependencies: List<Dependency<JClassVO>>, logicModules: List<LogicModule>, logicModuleDependencies: List<Dependency<LogicModule>>): List<Dependency<LogicModule>> {
        val interfaces = jClassRepository.getJClassesHasModules().filter { it.classType == ClazzType.INTERFACE }.map { "${it.module}.${it.name}" }
        return dependencies.flatMap { mapToModuleDependency(it, logicModules, interfaces) }
    }

    private fun mapToModuleDependency(dependency: Dependency<JClassVO>, logicModules: List<LogicModule>, interfaces: List<String>): List<Dependency<LogicModule>> {
        val callerClass = dependency.caller
        val calleeClass = dependency.callee

        val callerModules = getModule(logicModules, callerClass)
        val calleeModules = getModule(logicModules, calleeClass)

        // calleeClass不是接口类型，直接停止分析
        if (!isInterface(calleeClass, interfaces)) {
            return getModuleDependenciesByModules(callerModules, calleeModules)
        }

        val dubboAnalysisCalleeModules = dubboXmlDependencyAnalysisHelper.analysis(dependency, logicModules)
        if (dubboAnalysisCalleeModules.isEmpty()) {
            return getModuleDependenciesByModules(callerModules, calleeModules)
        }
        val calleeModulesAfterAnalysis = calleeModules.intersect(dubboAnalysisCalleeModules)
        return getModuleDependenciesByModules(callerModules, calleeModulesAfterAnalysis.toList())
    }

    private fun isInterface(jClassVO: JClassVO, interfaces: List<String>): Boolean{
        return interfaces.contains("${jClassVO.module}.${jClassVO.name}")
    }

    private fun getModuleDependenciesByModules(callerModules: List<LogicModule>, calleeModules: List<LogicModule>): List<Dependency<LogicModule>> {
        return callerModules.flatMap { caller -> calleeModules.map { callee -> Dependency(caller, callee) } }
    }
}
