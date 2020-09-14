package com.thoughtworks.archgard.scanner2.domain.service

import com.thoughtworks.archgard.scanner2.domain.CycleDetector
import com.thoughtworks.archgard.scanner2.domain.model.Graph
import com.thoughtworks.archgard.scanner2.domain.model.GraphStore
import com.thoughtworks.archgard.scanner2.domain.model.IdNode
import com.thoughtworks.archgard.scanner2.domain.model.JClassVO
import com.thoughtworks.archgard.scanner2.domain.model.JMethodVO
import com.thoughtworks.archgard.scanner2.domain.model.Node
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JMethodRepository
import org.springframework.stereotype.Service


@Service
class CircularDependencyService(val jClassRepository: JClassRepository, val jMethodRepository: JMethodRepository) {
    fun getClassCircularDependency(systemId: Long): List<List<JClassVO>> {
        val allClassDependencies = jClassRepository.getAllClassDependencies(systemId)
        val cycles = findCyclesFromDependencies(allClassDependencies)
        if (cycles.isEmpty()) {
            return emptyList()
        }
        val jClassesHasModules = jClassRepository.getJClassesHasModules(systemId)
        return cycles.map { it.map { jClassesHasModules.first { jClass -> jClass.id == it.getNodeId() }.toVO() } }
    }

    fun getMethodCircularDependency(systemId: Long): List<List<JMethodVO>> {
        val allMethodDependencies = jMethodRepository.getAllMethodDependencies(systemId)
        val cycles = findCyclesFromDependencies(allMethodDependencies)
        if (cycles.isEmpty()) {
            return emptyList()
        }
        val methodsHasModules = jMethodRepository.getMethodsHasModules(systemId)
        return cycles.map { it.map { methodsHasModules.first { jMethod -> jMethod.id == it.getNodeId() }.toVO() } }
    }

    private fun findCyclesFromDependencies(dependencies: List<Dependency<String>>): List<List<Node>> {
        val graph = buildGraph(dependencies)
        return CycleDetector(graph).findCycles()
    }

    private fun buildGraph(allClassDependencies: List<Dependency<String>>): Graph {
        val graph = GraphStore()
        allClassDependencies.forEach { graph.addEdge(IdNode(it.caller), IdNode(it.callee)) }
        return graph.toDirectedGraph()
    }
}