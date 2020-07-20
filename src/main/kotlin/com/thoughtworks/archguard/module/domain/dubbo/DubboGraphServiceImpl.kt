package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.module.domain.DefaultGraphService
import com.thoughtworks.archguard.module.domain.DependencyAnalysisHelper
import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.NoModuleFoundException
import com.thoughtworks.archguard.module.domain.dependency.DependencyService
import com.thoughtworks.archguard.module.domain.getModule
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.LogicModule
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
@Qualifier("Dubbo")
class DubboGraphServiceImpl(logicModuleRepository: LogicModuleRepository, dependencyService: DependencyService, val dubboXmlDependencyAnalysisHelper: DependencyAnalysisHelper) : DefaultGraphService(logicModuleRepository, dependencyService) {
    private val log = LoggerFactory.getLogger(DubboGraphServiceImpl::class.java)

    override fun mapClassDependencyToModuleDependency(logicModules: List<LogicModule>, jClassDependency: Dependency<JClass>): List<Dependency<LogicModule>> {
        val callerClass = jClassDependency.caller
        val calleeClass = jClassDependency.callee

        val callerModules: List<LogicModule>
        try {
            callerModules = getModule(logicModules, callerClass.toVO())

            if (callerModules.size > 1) {
                log.error("Caller Class belong to more than one Module!", callerModules)
            }
            val callerModule = callerModules[0]
            val calleeModules = getModule(logicModules, calleeClass.toVO())
            log.info("calleeModules before dubbo analysis: {}", calleeModules)

            // calleeClass不是接口类型，直接停止分析
            if (!calleeClass.isInterface()) {
                return calleeModules.map { Dependency(callerModule, it) }
            }

            val dubboAnalysisCalleeModules = dubboXmlDependencyAnalysisHelper.analysis(jClassDependency, logicModules)
            log.info("dubboAnalysisCalleeModules after dubbo analysis: {}", dubboAnalysisCalleeModules)

            if (dubboAnalysisCalleeModules.isEmpty()) {
                return calleeModules.map { Dependency(callerModule, it) }
            }
            val calleeModulesAfterAnalysis = calleeModules.intersect(dubboAnalysisCalleeModules)
            return calleeModulesAfterAnalysis.map { Dependency(callerModule, it) }
        } catch (e: NoModuleFoundException) {
            return emptyList()
        }
    }
}
