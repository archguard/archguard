package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.model.SubModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class BaseModuleService {
    @Autowired
    lateinit var logicModuleRepository: LogicModuleRepository

    fun getBaseModules(): List<SubModule> {
        return logicModuleRepository.getAllSubModule()
    }
}
