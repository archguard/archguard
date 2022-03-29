package org.archguard.scanner.sourcecode

import org.archguard.scanner.sourcecode.container.ContainerDemand
import org.archguard.scanner.sourcecode.container.ContainerResource
import org.archguard.scanner.sourcecode.container.ContainerService
import infrastructure.SourceBatch
import java.util.HashMap

class ContainerRepository(systemId: String, language: String, workspace: String) {
    private val batch: SourceBatch = SourceBatch()
    private val systemId: String
    private val language: String
    private val workspace: String

    init {
        this.systemId = systemId
        this.language = language
        this.workspace = workspace
    }

    fun saveContainerServices(services: Array<ContainerService>) {
        val serviceId = saveMainServices()
        services.forEach { caller ->
            caller.demands.map { saveDemand(it, serviceId, caller.name) }.toTypedArray()
            caller.resources.map { saveResource(it, serviceId, caller.name) }.toTypedArray()
        }
    }

    private fun saveMainServices(): String {
        val time: String = ClassRepository.currentTime
        val serviceId = ClassRepository.generateId()
        val values: MutableMap<String, String> = HashMap()

        values["id"] = serviceId
        values["system_id"] = systemId
        values["name"] = ""
        if(this.language.lowercase() == "typescript" || this.language.lowercase() == "javascript") {
            values["container_type"] = "Frontend"
        } else {
            values["container_type"] = "Backend"
        }

        values["scm_address"] = ""
        values["created_by"] = "scanner"

        values["updated_at"] = time
        values["created_at"] = time

        batch.add("container_service", values)
        return serviceId
    }


    private fun saveResource(it: ContainerResource, serviceId: String, name: String): String {
        val time: String = ClassRepository.currentTime
        val resourceId = ClassRepository.generateId()
        val values: MutableMap<String, String> = HashMap()

        values["id"] = resourceId
        values["service_id"] = serviceId
        values["system_id"] = systemId

        values["source_url"] = it.sourceUrl
        values["source_http_method"] = it.sourceHttpMethod

        values["package_name"] = it.packageName
        values["class_name"] = it.className
        values["method_name"] = it.methodName

        values["updated_at"] = time
        values["created_at"] = time
        values["created_by"] = "scanner"

        batch.add("container_resource", values)
        return resourceId
    }


    private fun saveDemand(demand: ContainerDemand, serviceId: String, name: String): String {
        val time: String = ClassRepository.currentTime
        val demandId = ClassRepository.generateId()
        val values: MutableMap<String, String> = HashMap()

        values["id"] = demandId

        values["target_http_method"] = demand.target_http_method
        values["target_url"] = "${demand.base}${demand.target_url}"
        val split = name.split("::")

        values["source_package"] = ""
        values["source_class"] = ""
        values["source_method"] = ""
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
        values["created_by"] = "scanner"

        batch.add("container_demand", values)
        return demandId
    }

    fun close() {
        batch.execute()
        batch.close()
    }
}