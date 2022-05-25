package com.thoughtworks.archguard.code.module.domain.dependency

import com.thoughtworks.archguard.code.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.code.module.domain.model.Dependency
import com.thoughtworks.archguard.code.module.domain.model.JClassVO
import com.thoughtworks.archguard.code.module.domain.model.JMethodVO
import com.thoughtworks.archguard.code.module.domain.plugin.DependPlugin
import com.thoughtworks.archguard.code.module.domain.plugin.PluginManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DependencyServiceImpl : DependencyService {

    @Autowired
    lateinit var logicModuleRepository: LogicModuleRepository

    @Autowired
    lateinit var dependencyRepository: DependencyRepository

    @Autowired
    lateinit var pluginManager: PluginManager

    override fun getAllMethodDependencies(systemId: Long): List<Dependency<JMethodVO>> {
        var methodDependencies = dependencyRepository.getAllMethodDependencies(systemId)

        pluginManager.getDependPlugin<DependPlugin>(systemId).forEach { methodDependencies = it.fixMethodDependencies(systemId, methodDependencies) }

        return methodDependencies
    }

    override fun getAllMethodDependencies(systemId: Long, caller: String, callee: String): List<Dependency<JMethodVO>> {
        val methodDependencies = getAllMethodDependencies(systemId)
        return methodDependencies.filter { it.caller.clazz.module == caller && it.callee.clazz.module == callee }
    }

    override fun getAllDistinctMethodDependencies(systemId: Long, caller: String, callee: String): List<Dependency<JMethodVO>> {
        return getAllMethodDependencies(systemId, caller, callee).toSet().toList()
    }

    override fun getAllWithFullNameStart(systemId: Long, callerStart: List<String>, calleeStart: List<String>): List<Dependency<JMethodVO>> {
        return getAllMethodDependencies(systemId).filter { method -> callerStart.any { method.caller.fullName.startsWith(it) } && calleeStart.any { method.callee.fullName.startsWith(it) } }
    }

    override fun getAllClassDependencies(systemId: Long): List<Dependency<JClassVO>> {
        return getAllMethodDependencies(systemId).map { Dependency(it.caller.clazz, it.callee.clazz) }
    }
}
