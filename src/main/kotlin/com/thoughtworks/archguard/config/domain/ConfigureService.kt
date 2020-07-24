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

    fun isDisplayClass(className: String): Boolean {
        val displayConfig = repo.getConfiguresByType("nodeDisplay").map { it.value } as MutableList
        val hiddenConfig = repo.getConfiguresByType("nodeHidden").map { it.value } as MutableList
        if (displayConfig.isEmpty()) {
            displayConfig.add(".")
        }
        hiddenConfig.add("Test")
        return displayConfig.any { className.contains(it) } && hiddenConfig.all { !className.contains(it) }
    }

    fun updateConfigsByType(type: String, configs: List<Configure>) {
        repo.deleteConfiguresByType(type)
        repo.batchCreateConfigures(configs)
    }
}
