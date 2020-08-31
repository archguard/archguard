package com.thoughtworks.archguard.module.domain.dependency

import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.getModule
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.plugin.DependPlugin
import com.thoughtworks.archguard.module.domain.plugin.PluginManager
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
        val callerLogicModule = logicModuleRepository.get(systemId, caller)
        val calleeLogicModule = logicModuleRepository.get(systemId, callee)
        val logicModules = logicModuleRepository.getAllBysystemId(systemId)

        return getAllMethodDependencies(systemId).filter { inModule(it.caller, callerLogicModule, logicModules) && inModule(it.callee, calleeLogicModule, logicModules) }
    }

    override fun getAllWithFullNameStart(systemId: Long, callerStart: List<String>, calleeStart: List<String>): List<Dependency<JMethodVO>> {
        return getAllMethodDependencies(systemId).filter { method -> callerStart.any { method.caller.fullName.startsWith(it) } && calleeStart.any { method.callee.fullName.startsWith(it) } }
    }


    override fun getAllClassDependencies(systemId: Long): List<Dependency<JClassVO>> {
        return getAllMethodDependencies(systemId).map { Dependency(it.caller.clazz, it.callee.clazz) }
    }

    private fun inModule(method: JMethodVO, logicModule: LogicModule, logicModules: List<LogicModule>): Boolean {
        return getModule(logicModules, method.clazz).contains(logicModule)
    }

}
