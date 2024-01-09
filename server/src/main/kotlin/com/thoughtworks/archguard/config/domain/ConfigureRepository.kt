package com.thoughtworks.archguard.config.domain

import org.archguard.config.Configure

interface ConfigureRepository {
    fun getConfigures(systemId: Long): List<Configure>
    fun create(config: Configure)
    fun update(config: Configure)
    fun delete(id: String)
    fun deleteConfiguresByType(systemId: Long, type: String)
    fun batchCreateConfigures(configs: List<Configure>)
}
