package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.module.domain.LogicModuleStatus

data class LogicModuleDTO(val id: String, val name: String, val members: String, val status: LogicModuleStatus)
