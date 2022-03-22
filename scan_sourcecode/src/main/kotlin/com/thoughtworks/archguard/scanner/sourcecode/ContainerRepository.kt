package com.thoughtworks.archguard.scanner.sourcecode

import com.thoughtworks.archguard.scanner.sourcecode.frontend.ContainerDemand
import com.thoughtworks.archguard.scanner.sourcecode.frontend.ContainerService
import infrastructure.SourceBatch
import java.util.HashMap

class ContainerRepository(systemId: String, language: String, workspace: String) {
    private val batch: SourceBatch = SourceBatch()
    private val batchStep = 100
    private val systemId: String
    private val language: String
    private val workspace: String

    init {
        this.systemId = systemId
        this.language = language
        this.workspace = workspace
    }

    fun saveContainerServices(services: Array<ContainerService>) {
        services.forEach { caller ->
            val serviceId = saveMainServices()
            caller.demands.map { saveDemand(it, serviceId, caller.name) }.toTypedArray()
        }
    }

    private fun saveMainServices(): String {
        val time: String = ClassRepository.currentTime
        val serviceId = ClassRepository.generateId()
        val values: MutableMap<String, String> = HashMap()

        values["id"] = serviceId
        values["system_id"] = systemId

        values["updated_at"] = time
        values["created_at"] = time

        batch.add("container_service", values)
        return serviceId
    }

    private fun saveDemand(demand: ContainerDemand, serviceId: String, name: String): String {
        val time: String = ClassRepository.currentTime
        val demandId = ClassRepository.generateId()
        val values: MutableMap<String, String> = HashMap()

        values["id"] = demandId

        values["target_http_method"] = demand.target_http_method
        values["target_url"] = demand.target_url
        val split = name.split("::")

        if (split.size == 2) {
            values["source_package"] = split[0]
            values["source_method"] = split[1]
        } else {
            values["source_method"] = name
        }

        values["service_id"] = serviceId
        values["system_id"] = systemId
        values["updated_at"] = time
        values["created_at"] = time

        batch.add("container_demand", values)
        return demandId
    }

    fun close() {
        batch.execute()
        batch.close()
    }
}