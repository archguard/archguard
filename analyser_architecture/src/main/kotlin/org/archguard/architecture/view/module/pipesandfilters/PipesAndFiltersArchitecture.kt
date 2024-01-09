package org.archguard.architecture.view.module.pipesandfilters

import org.archguard.architecture.view.module.DependencyType
import org.archguard.architecture.view.module.ModuleArchitecture

class PipesAndFiltersArchitecture : org.archguard.architecture.view.module.ModuleArchitecture {
    val pipes = mutableListOf<org.archguard.architecture.view.module.pipesandfilters.Pipe>()
    val filters = mutableListOf<org.archguard.architecture.view.module.pipesandfilters.Filter>()
    val dependencies = mutableListOf<org.archguard.architecture.view.module.pipesandfilters.Dependency>()
}

class Pipe(val name: String, val type: org.archguard.architecture.view.module.pipesandfilters.PipeType)

enum class PipeType {
    Data,
    Control
}

class Filter(val name: String, val type: org.archguard.architecture.view.module.pipesandfilters.FilterType)

enum class FilterType {
    Transform,
    Source,
    Sink
}

class Dependency(val type: org.archguard.architecture.view.module.DependencyType, val dependent: String, val dependence: String)
