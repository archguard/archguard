package org.archguard.scanner.analyser.backend

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.analyser.base.ApiAnalyser
import org.archguard.scanner.core.sourcecode.ContainerService
import org.archguard.scanner.core.sourcecode.ContainerSupply

class GoApiAnalyser: ApiAnalyser {
    override var resources: List<ContainerSupply> = listOf()

    override fun analysisByNode(node: CodeDataStruct, workspace: String) {

    }

    override fun toContainerServices(): List<ContainerService> {
        return listOf()
    }
}