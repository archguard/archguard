package org.archguard.architecture.core.module.view.module.pipesandfilters

import org.archguard.architecture.core.module.view.module.DependencyType
import org.archguard.architecture.core.module.view.module.ModuleArchitecture

class PipesAndFiltersArchitecture : ModuleArchitecture {
    val pipes = mutableListOf<Pipe>()
    val filters = mutableListOf<Filter>()
    val dependencies = mutableListOf<Dependency>()
}

class Pipe(val name: String, val type: PipeType)

enum class PipeType {
    Data,
    Control
}

class Filter(val name: String, val type: FilterType)

enum class FilterType {
    Transform,
    Source,
    Sink
}

class Dependency(val type: DependencyType, val dependent: String, val dependence: String)
