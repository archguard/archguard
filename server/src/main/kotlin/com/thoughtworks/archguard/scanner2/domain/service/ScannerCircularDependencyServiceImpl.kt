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
) : CircularDependencyServiceInterface() {
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

}
