package org.archguard.scanner.architecture.view.module

import org.archguard.scanner.architecture.detect.PotentialExecArch
import org.archguard.scanner.architecture.view.module.pipesandfilters.Filter
import org.archguard.scanner.architecture.view.module.pipesandfilters.Pipe
import org.archguard.scanner.architecture.view.module.shared.Dependency

class PipesAndFilterArchitecture : ArchitectureStyle {
    val pipes = mutableListOf<Pipe>()
    val filters = mutableListOf<Filter>()
    val dependencies = mutableListOf<Dependency>()
    override fun canBeApplied(identPotential: PotentialExecArch): Boolean {
        return false
    }
}

