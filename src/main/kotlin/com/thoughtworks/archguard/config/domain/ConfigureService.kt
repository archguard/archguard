package com.thoughtworks.archguard.config.domain

import org.springframework.stereotype.Service

@Service
class ConfigureService(val repo: ConfigureRepository) {

    fun getConfigures(systemId: Long): List<Configure> {
        return repo.getConfigures(systemId)
    }

    fun create(config: Configure) {
        repo.create(config)
    }

    fun update(id: String, config: Configure) {
        val nodeConfigure = Configure(id, config.systemId, config.type, config.key, config.value, config.order)
        repo.update(nodeConfigure)
    }

    fun delete(id: String) {
        repo.delete(id)
    }

    fun isDisplayNode(systemId: Long, nodeName: String): Boolean {
        if (nodeName.endsWith("Test")) return false

        val displayConfig = repo.getConfiguresByType(systemId, "nodeDisplay")
        val continueNodes = displayConfig.filter { it.value == "contain" }.map { it.key }
        val hiddenNodes = displayConfig.filter { it.value == "hidden" }.map { it.key }

        return (continueNodes.isEmpty() || continueNodes.any { nodeName.contains(it) }) && hiddenNodes.all { !nodeName.contains(it) }
    }


    fun updateConfigsByType(systemId: Long, type: String, configs: List<Configure>) {
        repo.deleteConfiguresByType(systemId, type)
        configs.forEach { it.systemId = systemId }
        repo.batchCreateConfigures(configs)
    }
}
