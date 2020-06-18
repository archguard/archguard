package com.thoughtworks.archguard.module.domain

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
