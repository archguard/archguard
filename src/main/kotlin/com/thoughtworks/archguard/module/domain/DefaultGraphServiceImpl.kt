package com.thoughtworks.archguard.module.domain

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
@Qualifier("Default")
class DefaultGraphServiceImpl(logicModuleRepository: LogicModuleRepository) : DefaultGraphService(logicModuleRepository) {

    private val log = LoggerFactory.getLogger(DefaultGraphServiceImpl::class.java)
    override fun mapClassDependencyToModuleDependency(logicModules: List<NewLogicModule>, jClassDependency: NewDependency<JClass>): List<NewDependency<NewLogicModule>> {
        val callerModules = getNewModule(logicModules, jClassDependency.caller)
        if (callerModules.size > 1) {
            log.error("Caller Class belong to more than one Module!", callerModules)
        }
        val callerModule = callerModules[0]
        val calleeModules = getNewModule(logicModules, jClassDependency.callee)

        return calleeModules.map { NewDependency(callerModule, it) }
    }
}