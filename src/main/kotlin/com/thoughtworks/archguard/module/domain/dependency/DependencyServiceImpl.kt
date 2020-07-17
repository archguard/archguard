package com.thoughtworks.archguard.module.domain.dependency

import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JMethod
import com.thoughtworks.archguard.module.domain.model.JMethodVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DependencyServiceImpl : DependencyService {

    @Autowired
    lateinit var logicModuleRepository: LogicModuleRepository

    @Autowired
    lateinit var dependencyRepository: DependencyRepository

    override fun getLogicModulesDependencies(caller: String, callee: String): List<Dependency<JMethod>> {
        return logicModuleRepository.getDependence(caller, callee)
    }

    override fun getAll(): List<Dependency<JMethodVO>> {
        return dependencyRepository.getAll()
    }

    override fun getAllWithFilter(callerFilter: List<String>, calleeFilter: List<String>): List<Dependency<JMethodVO>> {
        return getAll().filter { method ->  callerFilter.any { method.caller.fullName.startsWith(it) } && calleeFilter.any { method.callee.fullName.startsWith(it) } }
    }

}
