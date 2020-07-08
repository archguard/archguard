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

}
