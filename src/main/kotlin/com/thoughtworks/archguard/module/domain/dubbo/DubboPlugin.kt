package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.clazz.domain.ClazzType
import com.thoughtworks.archguard.clazz.domain.JClass
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
    lateinit var xmlConfigService: XmlConfigService

    @Autowired
    lateinit var jClassRepository: JClassRepository

    override fun mapToModuleDependencies(dependencies: List<Dependency<JClassVO>>, logicModules: List<LogicModule>, logicModuleDependencies: List<Dependency<LogicModule>>): List<Dependency<LogicModule>> {
        val interfaces = jClassRepository.getJClassesHasModules().filter { it.classType == ClazzType.INTERFACE }
        return dependencies.flatMap { mapToModuleDependency(it, logicModules, interfaces) }
    }

    private fun mapToModuleDependency(dependency: Dependency<JClassVO>, logicModules: List<LogicModule>, interfaces: List<JClass>): List<Dependency<LogicModule>> {
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

    private fun isInterface(jClassVO: JClassVO, interfaces: List<JClass>): Boolean{
        return interfaces.any { it.getFullName() == jClassVO.getFullName() }
    }

    private fun getModuleDependenciesByModules(callerModules: List<LogicModule>, calleeModules: List<LogicModule>): List<Dependency<LogicModule>> {
        return callerModules.flatMap { caller -> calleeModules.map { callee -> Dependency(caller, callee) } }
    }


    override fun fixClassDependencies(classDependencies: List<Dependency<JClassVO>>): List<Dependency<JClassVO>>{
        // A -> I , B : I
        // A -> B
        val interfaces = jClassRepository.getJClassesHasModules().filter { it.classType == ClazzType.INTERFACE }
        return classDependencies.flatMap { fixClassDependency(it, interfaces) }
    }

    private fun fixClassDependency(classDependency: Dependency<JClassVO>, interfaces: List<JClass>): List<Dependency<JClassVO>> {
        val caller = classDependency.caller
        val callee = classDependency.callee

        if (!isInterface(callee, interfaces) || caller.module == callee.module) {
            return listOf(classDependency)
        }
        return mapCalleeToReal(caller, callee).map { Dependency(caller, it) }
    }

    private fun mapCalleeToReal(caller: JClassVO, callee: JClassVO): List<JClassVO>{
        val implements = jClassRepository.findClassImplements(callee.name, callee.module).map { it.toVO() }
        val calleeSubModuleByXml = xmlConfigService.getRealCalleeModuleByXmlConfig(caller, callee)
        val realCallee = implements.filter { calleeSubModuleByXml.any{subModuleDubbo ->  subModuleDubbo.name == it.module} }
        if (realCallee.isEmpty()) {
            return implements
        }
        return realCallee
    }



}
