package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.getModule
import com.thoughtworks.archguard.module.domain.model.*
import com.thoughtworks.archguard.module.domain.plugin.AbstractDependPlugin
import com.thoughtworks.archguard.module.domain.plugin.PluginType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DubboPlugin : AbstractDependPlugin() {

    private val log = LoggerFactory.getLogger(DubboPlugin::class.java)

    @Autowired
    lateinit var xmlConfigService: XmlConfigService

    @Autowired
    lateinit var jClassRepository: JClassRepository

    override fun getPluginType(): PluginType {
        return PluginType.DUBBO
    }

    override fun fixMethodDependencies(methodDependencies: List<Dependency<JMethodVO>>): List<Dependency<JMethodVO>> {
        // A -> I, B : I
        // A -> B override
        val interfaces = jClassRepository.getJClassesHasModules().filter { it.isInterface() }
        return methodDependencies.flatMap { fixMethodDependency(it, interfaces) }
    }

    private fun isInterface(jClassVO: JClassVO, interfaces: List<JClass>): Boolean{
        return interfaces.any { it.getFullName() == jClassVO.getFullName() }
    }

    private fun fixMethodDependency(methodDependency: Dependency<JMethodVO>, interfaces: List<JClass>): List<Dependency<JMethodVO>> {
        val caller = methodDependency.caller
        val callee = methodDependency.callee

        if (!isInterface(callee.clazz, interfaces) || caller.clazz.module == callee.clazz.module) {
            return listOf(methodDependency)
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

    private fun mapCalleeToReal(caller: JMethodVO, callee: JMethodVO): List<JMethodVO> {
        val realCalleeClasses = mapCalleeToReal(caller.clazz, callee.clazz)
        return realCalleeClasses.map { JMethodVO(callee.name, it, callee.returnType, callee.argumentTypes) }
    }

}
