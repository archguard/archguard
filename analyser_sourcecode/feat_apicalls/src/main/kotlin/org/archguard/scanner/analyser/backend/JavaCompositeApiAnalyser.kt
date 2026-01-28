package org.archguard.scanner.analyser.backend

import chapi.domain.core.CodeDataStruct
import org.archguard.context.ContainerDemand
import org.archguard.context.ContainerService
import org.archguard.context.ContainerSupply
import org.archguard.scanner.analyser.base.ApiAnalyser

class JavaCompositeApiAnalyser : ApiAnalyser {
    override var resources: List<ContainerSupply> = listOf()
    var demands: List<ContainerDemand> = listOf()

    private var javaSpringAnalyser = JavaSpringAnalyser()
    private var javaDubboAnalyser = JavaDubboAnalyser()
    private var javaFeignClientAnalyser = JavaFeignClientAnalyser()

    override fun analysisByNode(node: CodeDataStruct, workspace: String) {
        javaSpringAnalyser.analysisByNode(node, workspace)
        javaDubboAnalyser.analysisByNode(node, workspace)
        javaFeignClientAnalyser.analysisByNode(node, workspace)
    }

    override fun toContainerServices(): List<ContainerService> {
        return javaSpringAnalyser.toContainerServices() + 
               javaDubboAnalyser.toContainerServices() + 
               javaFeignClientAnalyser.toContainerServices()
    }
}

