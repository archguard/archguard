package org.archguard.scanner.analyser.base

import chapi.domain.core.CodeDataStruct
import org.archguard.model.ContainerService
import org.archguard.model.ContainerSupply

interface ApiAnalyser {
    var resources: List<ContainerSupply>

    fun analysisByNode(node: CodeDataStruct, workspace: String)

    fun toContainerServices(): List<ContainerService>
}