package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.model.Graph
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.ModuleGraph

interface GraphServiceLegacy{
    @Deprecated(message = "we are going to replace with getLogicModuleGraph")
    fun getLogicModuleGraphLegacy(): ModuleGraph
    fun getLogicModuleGraph(): Graph<LogicModule>
}
