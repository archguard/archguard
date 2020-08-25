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

    override fun getAllMethodDependencies(projectId: Long): List<Dependency<JMethodVO>> {
        var methodDependencies =  dependencyRepository.getAllMethodDependencies()

        pluginManager.getDependPlugin<DependPlugin>(projectId).forEach { methodDependencies = it.fixMethodDependencies(projectId, methodDependencies) }

        return methodDependencies
    }

    override fun getAllMethodDependencies(projectId: Long, caller: String, callee: String): List<Dependency<JMethodVO>> {
        val callerLogicModule = logicModuleRepository.get(caller)
        val calleeLogicModule = logicModuleRepository.get(callee)
        val logicModules = logicModuleRepository.getAllByProjectId(projectId)

        return getAllMethodDependencies(projectId).filter { inModule(it.caller, callerLogicModule, logicModules) && inModule(it.callee, calleeLogicModule, logicModules) }
    }

    override fun getAllWithFullNameStart(projectId: Long, callerStart: List<String>, calleeStart: List<String>): List<Dependency<JMethodVO>>{
        return getAllMethodDependencies(projectId).filter { method -> callerStart.any { method.caller.fullName.startsWith(it) } && calleeStart.any { method.callee.fullName.startsWith(it) } }
    }


    override fun getAllClassDependencies(projectId: Long): List<Dependency<JClassVO>> {
        return getAllMethodDependencies(projectId).map { Dependency(it.caller.clazz, it.callee.clazz) }
    }

    private fun inModule(method: JMethodVO, logicModule: LogicModule, logicModules: List<LogicModule>): Boolean {
        return getModule(logicModules, method.clazz).contains(logicModule)
    }

}
