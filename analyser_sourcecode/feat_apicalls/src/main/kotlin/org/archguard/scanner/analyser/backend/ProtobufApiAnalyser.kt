package org.archguard.scanner.analyser.backend

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.analyser.base.ApiAnalyser
import org.archguard.scanner.core.sourcecode.ContainerDemand
import org.archguard.scanner.core.sourcecode.ContainerService
import org.archguard.scanner.core.sourcecode.ContainerSupply

class ProtobufApiAnalyser : ApiAnalyser {
    override var resources: List<ContainerSupply> = listOf()
    private var demands: List<ContainerDemand> = listOf()

    override fun analysisByNode(node: CodeDataStruct, workspace: String) {
        node.Functions.map {
            resources += ContainerSupply(
                sourceUrl = it.Name,
                sourceHttpMethod = it.Type.toString(),
                packageName = it.Package,
                className = "",
                methodName = ""
            )
        }
    }

    override fun toContainerServices(): List<ContainerService> {
        return mutableListOf(
            ContainerService(
                name = "",
                resources = resources,
                demands = demands
            )
        )
    }
}
