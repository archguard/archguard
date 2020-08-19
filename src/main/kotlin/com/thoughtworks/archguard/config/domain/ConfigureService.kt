package com.thoughtworks.archguard.config.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ConfigureService {
    @Autowired
    private lateinit var repo: ConfigureRepository

    fun getConfigures(projectId: Long): List<Configure> {
        return repo.getConfigures(projectId)
    }

    fun create(config: Configure) {
        repo.create(config)
    }

    fun update(id: String, config: Configure) {
        val nodeConfigure = Configure(id, config.projectId, config.type, config.key, config.value, config.order)
        repo.update(nodeConfigure)
    }

    fun delete(id: String) {
        repo.delete(id)
    }

    fun isDisplayNode(projectId: Long, nodeName: String): Boolean {
        if (nodeName.endsWith("Test")) return false

        val displayConfig = repo.getConfiguresByType(projectId,"nodeDisplay")
        val continueNodes = displayConfig.filter { it.value == "contain" }.map { it.key }
        val hiddenNodes = displayConfig.filter { it.value == "hidden" }.map { it.key }

        return (continueNodes.isEmpty() || continueNodes.any{ nodeName.contains(it) }) && hiddenNodes.all { !nodeName.contains(it) }
    }


    fun updateConfigsByType(projectId: Long, type: String, configs: List<Configure>) {
        repo.deleteConfiguresByType(projectId, type)
        repo.batchCreateConfigures(configs)
    }
}
