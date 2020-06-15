package com.thoughtworks.archguard.dependence.infrastructure.logic_module

import com.thoughtworks.archguard.dependence.domain.logic_module.LogicModuleStatus

data class LogicModuleDTO(val id: String, val name: String, val members: String, val status: LogicModuleStatus)
