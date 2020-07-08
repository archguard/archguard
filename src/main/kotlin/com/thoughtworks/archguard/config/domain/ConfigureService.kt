package com.thoughtworks.archguard.config.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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

    fun update(id:String, config: NodeConfigure) {
        val nodeConfigure = NodeConfigure(id, config.type, config.key, config.value, config.order)
        repo.update(nodeConfigure)
    }

    fun delete(id: String) {
        repo.delete(id)
    }
}
