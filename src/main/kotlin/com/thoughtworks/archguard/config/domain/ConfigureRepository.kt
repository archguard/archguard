package com.thoughtworks.archguard.config.domain

interface ConfigureRepository {
    fun getConfigures(projectId: Long): List<Configure>
    fun create(config: Configure)
    fun update(config: Configure)
    fun delete(id: String)
    fun getConfiguresByType(projectId: Long, type: String): List<Configure>
    fun deleteConfiguresByType(projectId: Long, type: String)
    fun batchCreateConfigures(configs: List<Configure>)
}
