package com.thoughtworks.archguard.config.domain

import com.sun.org.apache.xpath.internal.operations.Bool
import com.thoughtworks.archguard.module.domain.model.JClass
import org.apache.logging.log4j.util.Strings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ConfigureService {
    @Autowired
    private lateinit var repo: ConfigureRepository

    fun getConfigures(): List<NodeConfigure> {
        return repo.getConfigures()
    }

    fun create(config: NodeConfigure) {
        repo.create(config)
    }

    fun update(id: String, config: NodeConfigure) {
        val nodeConfigure = NodeConfigure(id, config.type, config.key, config.value, config.order)
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

    fun updateConfigsByType(type: String, configs: List<NodeConfigure>) {
        repo.deleteConfiguresByType(type)
        repo.batchCreateConfigures(configs)
    }
}
