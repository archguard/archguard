package com.thoughtworks.archguard.module.domain.metrics.abstracts

import com.thoughtworks.archguard.module.domain.model.LogicModule

data class ModuleMetrics(val ratio: Double, val logicModule: LogicModule)