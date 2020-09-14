package com.thoughtworks.archgard.scanner2.domain.service

import com.thoughtworks.archgard.scanner2.domain.CycleDetector
import com.thoughtworks.archgard.scanner2.domain.model.GraphStore
import com.thoughtworks.archgard.scanner2.domain.model.IdNode
import com.thoughtworks.archgard.scanner2.domain.model.JClassVO
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import org.springframework.stereotype.Service


@Service
class CircularDependencyService(val jClassRepository: JClassRepository) {
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
}