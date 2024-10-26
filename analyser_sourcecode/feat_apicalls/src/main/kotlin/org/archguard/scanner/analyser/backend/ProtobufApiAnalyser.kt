package org.archguard.scanner.analyser.backend

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.analyser.base.ApiAnalyser
import org.archguard.context.ContainerDemand
import org.archguard.context.ContainerService
import org.archguard.context.ContainerSupply

class ProtobufApiAnalyser : ApiAnalyser {
    override var resources: List<ContainerSupply> = listOf()
    private var demands: List<ContainerDemand> = listOf()

    override fun analysisByNode(node: CodeDataStruct, workspace: String) {
        node.Functions.map {
            resources += ContainerSupply(
                sourceUrl = ("""rpc.${it.Package}.${node.NodeName}.${it.Name}""").replace(".", "/"),
                sourceHttpMethod = it.Type.toString(),
                packageName = it.Package,
                className = node.NodeName,
                methodName = it.Name,
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
