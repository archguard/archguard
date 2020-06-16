package com.thoughtworks.archguard.dependence.infrastructure.module

import com.thoughtworks.archguard.dependence.domain.module.LogicModuleStatus

data class LogicModuleDTO(val id: String, val name: String, val members: String, val status: LogicModuleStatus)
