package com.thoughtworks.archguard.config.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ConfigureService {
    @Autowired
    private lateinit var repo: ConfigureRepository

    fun getConfigures(): List<Configure> {
        return repo.getConfigures()
    }

    fun create(config: Configure) {
        repo.create(config)
    }

    fun update(id: String, config: Configure) {
        val nodeConfigure = Configure(id, config.type, config.key, config.value, config.order)
        repo.update(nodeConfigure)
    }

    fun delete(id: String) {
        repo.delete(id)
    }

    fun isDisplayNode(nodeName: String): Boolean {
        if (nodeName.endsWith("Test")) return false

        val displayConfig = repo.getConfiguresByType("nodeDisplay")
        val continueNodes = displayConfig.filter { it.value == "contain" }.map { it.key }
        val hiddenNodes = displayConfig.filter { it.value == "hidden" }.map { it.key }

        return (continueNodes.isEmpty() || continueNodes.any{ nodeName.contains(it) }) && hiddenNodes.all { !nodeName.contains(it) }
    }


    fun updateConfigsByType(type: String, configs: List<Configure>) {
        repo.deleteConfiguresByType(type)
        repo.batchCreateConfigures(configs)
    }
}
