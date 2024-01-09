package com.thoughtworks.archguard.code.module.domain.dubbo

import com.thoughtworks.archguard.code.clazz.domain.JClass
import com.thoughtworks.archguard.code.clazz.domain.JClassRepository
import com.thoughtworks.archguard.code.module.domain.plugin.AbstractDependPlugin
import org.archguard.plugin.PluginType
import org.archguard.model.Dependency
import org.archguard.model.vos.JClassVO
import org.archguard.model.vos.JMethodVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DubboPlugin : AbstractDependPlugin() {

    @Autowired
    lateinit var jClassRepository: JClassRepository

    override fun getPluginType(): PluginType {
        return PluginType.DUBBO
    }

    override fun fixMethodDependencies(systemId: Long, methodDependencies: List<Dependency<JMethodVO>>): List<Dependency<JMethodVO>> {
        // A -> I, B : I
        // A -> B override
        val interfaces = jClassRepository.getJClassesHasModules(systemId).filter { it.isInterface() }
        return methodDependencies.flatMap { fixMethodDependency(systemId, it, interfaces) }
    }

    private fun isInterface(jClassVO: JClassVO, interfaces: List<JClass>): Boolean {
        return interfaces.any { it.getFullName() == jClassVO.getFullName() }
    }

    private fun fixMethodDependency(systemId: Long, methodDependency: Dependency<JMethodVO>, interfaces: List<JClass>): List<Dependency<JMethodVO>> {
        val caller = methodDependency.caller
        val callee = methodDependency.callee

        if (!isInterface(callee.clazz, interfaces) || caller.clazz.module == callee.clazz.module || caller.clazz.module == null || callee.clazz.module == null) {
            return listOf(methodDependency)
        }
        return mapCalleeToReal(systemId, caller, callee).map { Dependency(caller, it) }
    }

    fun mapCalleeToReal(
        systemId: Long,
        caller: JClassVO,
        callee: JClassVO
    ): List<JClassVO> {
        return jClassRepository.findClassImplements(systemId, callee.name, callee.module!!).map { it.toVO() }
    }

    private fun mapCalleeToReal(
        systemId: Long,
        caller: JMethodVO,
        callee: JMethodVO
    ): List<JMethodVO> {
        val realCalleeClasses = mapCalleeToReal(systemId, caller.clazz, callee.clazz)
        return realCalleeClasses.map {
            JMethodVO(
                callee.name,
                it,
                callee.returnType,
                callee.argumentTypes
            )
        }
    }
}
