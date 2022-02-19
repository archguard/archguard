package com.thoughtworks.archgard.scanner2.domain.service

import com.thoughtworks.archgard.scanner2.domain.model.GraphStore
import com.thoughtworks.archgard.scanner2.domain.model.JClass
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JMethodRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class LCOM4Service(val jClassRepository: JClassRepository,
                   val jMethodRepository: JMethodRepository) {

    private val log = LoggerFactory.getLogger(LCOM4Service::class.java)

    suspend fun calculate(systemId: Long, jClasses: List<JClass>): Map<String, Int> {
        jClasses.forEach { prepareJClassBasicDataForLCOM4(systemId, it) }

        val lcom4Map: MutableMap<String, Int> = mutableMapOf()
        jClasses.forEach { lcom4Map[it.id] = getLCOM4Graph(it).getConnectivityCount() }
        log.info("Finish calculate all lcom4, count: {}", lcom4Map.keys.size)

        return lcom4Map
    }

    private fun prepareJClassBasicDataForLCOM4(systemId: Long, jClass: JClass) {
        jClass.fields = jClassRepository.findFields(jClass.id)
        val methods = jMethodRepository.findMethodsByModuleAndClass(systemId, jClass.module, jClass.name)
        methods.forEach { it.fields = jMethodRepository.findMethodFields(it.id) }
        methods.forEach { it.callees = jMethodRepository.findMethodCallees(it.id) }
        jClass.methods = methods
    }

    fun getLCOM4Graph(jClass: JClass): GraphStore {
        val graphStore = GraphStore()
        val methods = jClass.methods
        methods.forEach { method ->
            method.fields.forEach { graphStore.addEdge(method.toVO(), it) }
            val methodsCallBySelfOtherMethod = method.callees.filter { jMethod -> methods.map { it.id }.contains(jMethod.id) }
            methodsCallBySelfOtherMethod.forEach { graphStore.addEdge(method.toVO(), it.toVO()) }
        }
        return graphStore
    }
}