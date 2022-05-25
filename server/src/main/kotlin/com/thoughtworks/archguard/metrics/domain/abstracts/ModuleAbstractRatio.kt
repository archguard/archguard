package com.thoughtworks.archguard.metrics.domain.abstracts

import com.thoughtworks.archguard.code.module.domain.model.LogicModule

data class ModuleAbstractRatio(val ratio: Double, val logicModule: LogicModule)
