package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.model.ModuleGraph

interface GraphService {
    fun getLogicModuleGraph(): ModuleGraph
}