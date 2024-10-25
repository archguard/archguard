package org.archguard.scanner.analyser.backend

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.analyser.base.ApiAnalyser
import org.archguard.scanner.core.sourcecode.ContainerDemand
import org.archguard.scanner.core.sourcecode.ContainerService
import org.archguard.scanner.core.sourcecode.ContainerSupply

class JavaCompositeApiAnalyser : ApiAnalyser {
    override var resources: List<ContainerSupply> = listOf()
    var demands: List<ContainerDemand> = listOf()

    var javaSpringAnalyser = JavaSpringAnalyser()
    var javaDubboAnalyser = JavaDubboAnalyser()

    override fun analysisByNode(node: CodeDataStruct, workspace: String) {
        javaSpringAnalyser.analysisByNode(node, workspace)
        javaDubboAnalyser.analysisByNode(node, workspace)
    }

    override fun toContainerServices(): List<ContainerService> {
        return javaSpringAnalyser.toContainerServices() + javaDubboAnalyser.toContainerServices()
    }
}

