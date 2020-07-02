package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.module.domain.DefaultGraphService
import com.thoughtworks.archguard.module.domain.Dependency
import com.thoughtworks.archguard.module.domain.DependencyAnalysisHelper
import com.thoughtworks.archguard.module.domain.LogicModule
import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.getModule
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
@Qualifier("Dubbo")
class DubboGraphServiceImpl(logicModuleRepository: LogicModuleRepository) : DefaultGraphService(logicModuleRepository) {
    @Autowired
    lateinit var dubboXmlDependencyAnalysisHelper: DependencyAnalysisHelper

    private val log = LoggerFactory.getLogger(DubboGraphServiceImpl::class.java)

    override fun mapClassDependencyToModuleDependency(logicModules: List<LogicModule>, classDependency: Dependency): List<Dependency> {
        val callerModules = getModule(logicModules, classDependency.caller)
        if (callerModules.size > 1) {
            log.error("Caller Class belong to more than one Module!", callerModules)
        }
        val callerModule = callerModules[0]
        val calleeModules = getModule(logicModules, classDependency.callee)
        log.info("calleeModules before dubbo analysis: {}", calleeModules)
        val dubboAnalysisCalleeModules = dubboXmlDependencyAnalysisHelper.analysis(classDependency, logicModules, calleeModules)
        log.info("dubboAnalysisCalleeModules after dubbo analysis: {}", dubboAnalysisCalleeModules)
        return dubboAnalysisCalleeModules.map { Dependency(callerModule, it) }
    }
}