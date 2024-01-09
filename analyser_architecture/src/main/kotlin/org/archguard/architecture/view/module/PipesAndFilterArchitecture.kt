package org.archguard.architecture.view.module

import org.archguard.architecture.view.module.pipesandfilters.Filter
import org.archguard.architecture.view.module.pipesandfilters.Pipe
import org.archguard.architecture.view.module.shared.Dependency

class PipesAndFilterArchitecture : ModuleArchitecture {
    val pipes = mutableListOf<Pipe>()
    val filters = mutableListOf<Filter>()
    val dependencies = mutableListOf<Dependency>()
}

