package org.archguard.scanner.analyser.backend

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.analyser.base.ApiAnalyser
import org.archguard.context.ContainerDemand
import org.archguard.context.ContainerService
import org.archguard.context.ContainerSupply
import org.archguard.context.ServiceSupplyType

class ProtobufApiAnalyser : ApiAnalyser {
    override var resources: List<ContainerSupply> = listOf()
    private var demands: List<ContainerDemand> = listOf()
    private var workspace: String = ""

    override fun analysisByNode(node: CodeDataStruct, workspace: String) {
        this.workspace = workspace
        node.Functions.map {
            resources += ContainerSupply(
                sourceUrl = ("""rpc.${it.Package}.${node.NodeName}.${it.Name}""").replace(".", "/"),
                sourceHttpMethod = it.Type.toString(),
                packageName = it.Package,
                className = node.NodeName,
                methodName = it.Name,
                supplyType = ServiceSupplyType.PROTO_RPC_API
            )
        }
    }

    override fun toContainerServices(): List<ContainerService> {
        /// get last part of the workspace path
        val workspaceName = workspace.split("/").last()
        return mutableListOf(
            ContainerService(
                name = workspaceName,
                resources = resources,
                demands = demands
            )
        )
    }
}
