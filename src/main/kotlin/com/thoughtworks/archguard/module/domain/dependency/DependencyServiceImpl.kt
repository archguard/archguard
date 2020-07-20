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

    override fun getAllWithFullNameRegex(callerRegex: List<Regex>, calleeRegex: List<Regex>): List<Dependency<JMethodVO>> {
        return getAll().filter { method ->  callerRegex.any { it.matches(method.caller.fullName) } && calleeRegex.any { it.matches(method.callee.fullName) }}
    }

    override fun getAllWithFullNameStart(callerStart: List<String>, calleeStart: List<String>): List<Dependency<JMethodVO>>{
        return getAll().filter { method -> callerStart.any { method.caller.fullName.startsWith(it) } && calleeStart.any { method.callee.fullName.startsWith(it) } }
    }


}
