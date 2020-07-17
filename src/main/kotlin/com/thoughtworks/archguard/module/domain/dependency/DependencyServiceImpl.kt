package com.thoughtworks.archguard.module.domain.dependency

import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JMethod
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DependencyServiceImpl : DependencyService {
    @Autowired
    lateinit var logicModuleRepository: LogicModuleRepository

    override fun getLogicModulesDependencies(caller: String, callee: String): List<Dependency<JMethod>> {
        return logicModuleRepository.getDependence(caller, callee)
    }
}
