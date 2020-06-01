package com.thoughtworks.archguard.dependence.domain.base_module

import com.thoughtworks.archguard.dependence.domain.logic_module.LogicModule
import com.thoughtworks.archguard.dependence.domain.logic_module.ModuleDependency
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class BaseModuleService {
    @Autowired
    lateinit var baseModuleRepository: BaseModuleRepository

    fun getBaseModules(): List<String> {
        return baseModuleRepository.getBaseModules()
    }
}
