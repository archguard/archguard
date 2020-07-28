package com.thoughtworks.archguard.module.domain.dependency

import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.JMethodLegacy
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import com.thoughtworks.archguard.module.domain.model.LogicComponent
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

    override fun getLogicModulesDependencies(caller: String, callee: String): List<Dependency<JMethodLegacy>> {
        return logicModuleRepository.getDependence(caller, callee)
    }

    override fun getAllMethodDependencies(): List<Dependency<JMethodVO>> {
        var methodDependencies =  dependencyRepository.getAllMethodDependencies()

        pluginManager.getPlugins().forEach { methodDependencies = it.fixMethodDependencies(methodDependencies) }

        return methodDependencies
    }

    override fun getAllWithFullNameStart(callerStart: List<String>, calleeStart: List<String>): List<Dependency<JMethodVO>>{
        return getAllMethodDependencies().filter { method -> callerStart.any { method.caller.fullName.startsWith(it) } && calleeStart.any { method.callee.fullName.startsWith(it) } }
    }


    override fun getAllClassDependencies(): List<Dependency<JClassVO>> {
        return getAllMethodDependencies().map { Dependency(it.caller.jClassVO, it.callee.jClassVO) }
    }


    override fun getAllClassDependencyLegacy(members: List<LogicComponent>): List<Dependency<JClassVO>>{
        return dependencyRepository.getAllClassDependencyLegacy(members)
    }


}
