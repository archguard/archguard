package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.sourcecode.ContainerService
import org.archguard.scanner.core.sourcecode.ContainerSupply

interface ApiAnalyser {
    var resources: List<ContainerSupply>

    fun analysisByNode(node: CodeDataStruct, workspace: String)

    fun toContainerServices(): List<ContainerService>
}