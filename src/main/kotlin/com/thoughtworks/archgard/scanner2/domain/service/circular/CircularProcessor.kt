package com.thoughtworks.archgard.scanner2.domain.service.circular

import com.thoughtworks.archgard.scanner2.domain.CycleDetector
import com.thoughtworks.archgard.scanner2.domain.model.Dependency
import com.thoughtworks.archgard.scanner2.domain.model.Graph
import com.thoughtworks.archgard.scanner2.domain.model.GraphStore
import com.thoughtworks.archgard.scanner2.domain.model.IdNode
import com.thoughtworks.archgard.scanner2.domain.model.JClassVO
import com.thoughtworks.archgard.scanner2.domain.model.Node
import com.thoughtworks.archgard.scanner2.domain.repository.CircularDependencyMetricRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository

abstract class CircularProcessor(val jClassRepository: JClassRepository,
                                 val circularDependencyMetricRepository: CircularDependencyMetricRepository) {

    abstract fun process(systemId: Long): List<List<Any>>;

    fun findCyclesFromDependencies(dependencies: Collection<Dependency<String>>): List<List<Node>> {
        val graph = buildGraph(dependencies)
        return CycleDetector(graph).findCycles()
    }

    fun buildGraph(allClassDependencies: Collection<Dependency<String>>): Graph {
        val graph = GraphStore()
        allClassDependencies.forEach { graph.addEdge(IdNode(it.caller), IdNode(it.callee)) }
        return graph.toDirectedGraph()
    }

    fun buildAllClassDependencies(systemId: Long): List<Dependency<JClassVO>> {
        val allClassIdDependencies = jClassRepository.getDistinctClassDependenciesAndNotThirdParty(systemId)
        val jClassesHasModules = jClassRepository.getJClassesNotThirdPartyAndNotTest(systemId)
        return allClassIdDependencies.map { dependency: Dependency<String> ->
            Dependency(
                    jClassesHasModules.first { jClass -> jClass.id == dependency.caller }.toVO(),
                    jClassesHasModules.first { jClass -> jClass.id == dependency.callee }.toVO()
            )
        }
    }
}