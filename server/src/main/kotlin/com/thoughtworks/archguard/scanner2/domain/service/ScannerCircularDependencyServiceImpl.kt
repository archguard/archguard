package com.thoughtworks.archguard.scanner2.domain.service

import org.archguard.operator.CycleDetector
import org.archguard.model.Dependency
import org.archguard.graph.Graph
import org.archguard.graph.GraphStore
import org.archguard.graph.IdNode
import org.archguard.model.vos.JClassVO
import org.archguard.model.vos.JMethodVO
import org.archguard.graph.Node
import com.thoughtworks.archguard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archguard.scanner2.domain.repository.JMethodRepository
import com.thoughtworks.archguard.scanner2.domain.Toggle
import org.archguard.wrapper.CircularDependencyServiceInterface
import org.springframework.stereotype.Service

@Service
class ScannerCircularDependencyServiceImpl(
    private val jClassRepository: JClassRepository,
    private val jMethodRepository: JMethodRepository
) : CircularDependencyServiceInterface {
    override fun getMethodsHasModules(systemId: Long) =
        jMethodRepository.getMethodsNotThirdParty(systemId)

    override fun getJClassesHasModules(systemId: Long) =
        jClassRepository.getJClassesNotThirdPartyAndNotTest(systemId)

    override fun getAllClassIdDependencies(systemId: Long) =
        jClassRepository.getDistinctClassDependenciesAndNotThirdParty(systemId)

    override fun getAllMethodDependencies(systemId: Long) =
        jMethodRepository.getDistinctMethodDependenciesAndNotThirdParty(systemId)

    fun getClassCircularDependency(systemId: Long): List<List<JClassVO>> {
        val isExcludeInternalClass = Toggle.EXCLUDE_INTERNAL_CLASS_CYCLE_DEPENDENCY.getStatus()
        return getClassCircularDependency(systemId, isExcludeInternalClass)
    }

    private fun getClassCircularDependency(systemId: Long, isExcludeInternalClass: Boolean): List<List<JClassVO>> {
        val allClassDependencies = getAllClassIdDependencies(systemId)
        val cycles = findCyclesFromDependencies(allClassDependencies)
        val jClassesHasModules = getJClassesHasModules(systemId)
        if (cycles.isEmpty()) {
            return emptyList()
        }

        val cycleList =
            cycles.map { it.map { JClassVO.fromClass(jClassesHasModules.first { jClass -> jClass.id == it.getNodeId() }) } }
        return if (isExcludeInternalClass) {
            cycleList.filter { cycle -> !isInternalClassCycle(cycle) }
        } else {
            cycleList
        }
    }

    private fun isInternalClassCycle(jClassList: List<JClassVO>): Boolean {
        return when (jClassList.map { it.getBaseClassName() }.toSet().size) {
            1 -> true
            else -> false
        }
    }

    fun getMethodCircularDependency(systemId: Long): List<List<JMethodVO>> {
        val allMethodDependencies = getAllMethodDependencies(systemId)
        val cycles = findCyclesFromDependencies(allMethodDependencies)
        if (cycles.isEmpty()) {
            return emptyList()
        }
        val methodsHasModules = getMethodsHasModules(systemId)
        return cycles.map { it.map { JMethodVO.fromJMethod(methodsHasModules.first { jMethod -> jMethod.id == it.getNodeId() }) } }
    }


    fun getModuleCircularDependency(systemId: Long): List<List<String>> {
        val moduleDependencies = buildModuleDependencies(systemId)
        val cycles = findCyclesFromDependencies(moduleDependencies)
        if (cycles.isEmpty()) {
            return emptyList()
        }
        return cycles.map { nodes -> nodes.map { it.getNodeId() } }
    }

    fun getPackageCircularDependency(systemId: Long): List<List<String>> {
        val packageDependencies = buildPackageDependencies(systemId)
        val cycles = findCyclesFromDependencies(packageDependencies)
        if (cycles.isEmpty()) {
            return emptyList()
        }
        return cycles.map { it.map { it.getNodeId() } }
    }

    private fun buildModuleDependencies(systemId: Long): MutableSet<Dependency<String>> {
        val allClassDependencies = buildAllClassDependencies(systemId)
        val moduleDependencies = mutableSetOf<Dependency<String>>()
        allClassDependencies.forEach {
            if (it.caller.module!! != it.callee.module!!) {
                moduleDependencies.add(Dependency(it.caller.module!!, it.callee.module!!))
            }
        }
        return moduleDependencies
    }

    private fun buildPackageDependencies(systemId: Long): MutableSet<Dependency<String>> {
        val allClassDependencies = buildAllClassDependencies(systemId)
        val packageDependencies = mutableSetOf<Dependency<String>>()
        allClassDependencies.forEach {
            val callerPackageName = "${it.caller.module}.${it.caller.getPackageName()}"
            val calleePackageName = "${it.caller.module}.${it.callee.getPackageName()}"
            if (callerPackageName != calleePackageName) {
                packageDependencies.add(Dependency(callerPackageName, calleePackageName))
            }
        }
        return packageDependencies
    }

    private fun buildAllClassDependencies(systemId: Long): List<Dependency<JClassVO>> {
        val allClassIdDependencies = getAllClassIdDependencies(systemId)
        val jClassesHasModules = getJClassesHasModules(systemId)
        return allClassIdDependencies.map { dependency: Dependency<String> ->
            Dependency(
                JClassVO.fromClass(jClassesHasModules.first { jClass -> jClass.id == dependency.caller }),
                JClassVO.fromClass(jClassesHasModules.first { jClass -> jClass.id == dependency.callee })
            )
        }
    }

    private fun findCyclesFromDependencies(dependencies: Collection<Dependency<String>>): List<List<Node>> {
        val graph = buildGraph(dependencies)
        return CycleDetector(graph).findCycles()
    }

    private fun buildGraph(allClassDependencies: Collection<Dependency<String>>): Graph {
        val graph = GraphStore()
        allClassDependencies.forEach { graph.addEdge(IdNode(it.caller), IdNode(it.callee)) }
        return graph.toDirectedGraph()
    }
}
