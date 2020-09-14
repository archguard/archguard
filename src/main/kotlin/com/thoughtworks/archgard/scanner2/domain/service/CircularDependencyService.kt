package com.thoughtworks.archgard.scanner2.domain.service

import com.thoughtworks.archgard.scanner2.domain.CycleDetector
import com.thoughtworks.archgard.scanner2.domain.model.GraphStore
import com.thoughtworks.archgard.scanner2.domain.model.IdNode
import com.thoughtworks.archgard.scanner2.domain.model.JClassVO
import com.thoughtworks.archgard.scanner2.domain.model.JMethodVO
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JMethodRepository
import org.springframework.stereotype.Service


@Service
class CircularDependencyService(val jClassRepository: JClassRepository, val jMethodRepository: JMethodRepository) {
    fun getClassCircularDependency(systemId: Long): List<List<JClassVO>> {
        val allClassDependencies = jClassRepository.getAllClassDependencies(systemId)
        val graph = GraphStore()
        allClassDependencies.forEach { graph.addEdge(IdNode(it.caller), IdNode(it.callee)) }
        val cycleDetector = CycleDetector(graph.toDirectedGraph())
        val cycles = cycleDetector.findCycles()
        val jClassesHasModules = jClassRepository.getJClassesHasModules(systemId)
        val jClassVOCycles = mutableListOf<List<JClassVO>>()
        cycles.forEach {
            val cycle = it.map { jClassesHasModules.first { jClass -> jClass.id == it.getNodeId() }.toVO() }
            jClassVOCycles.add(cycle)
        }
        return jClassVOCycles
    }

    fun getMethodCircularDependency(systemId: Long): List<List<JMethodVO>> {
        val allMethodDependencies = jMethodRepository.getAllMethodDependencies(systemId)
        val graph = GraphStore()
        allMethodDependencies.forEach { graph.addEdge(IdNode(it.caller), IdNode(it.callee)) }
        val cycleDetector = CycleDetector(graph.toDirectedGraph())
        val cycles = cycleDetector.findCycles()
        val methodsHasModules = jMethodRepository.getMethodsHasModules(systemId)
        val jMethodVOCycles = mutableListOf<List<JMethodVO>>()
        cycles.forEach {
            val cycle = it.map { methodsHasModules.first { jClass -> jClass.id == it.getNodeId() }.toVO() }
            jMethodVOCycles.add(cycle)
        }
        return jMethodVOCycles
    }
}