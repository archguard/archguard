package com.thoughtworks.archguard.config.domain

import org.archguard.config.ConfigType
import org.archguard.config.Configure
import org.springframework.stereotype.Service

@Service
class ConfigureService(val configureRepository: ConfigureRepository) {

    fun getConfigures(systemId: Long): List<Configure> {
        return configureRepository.getConfigures(systemId)
    }

    fun getDisPlayConfigures(systemId: Long): List<Configure> {
        return configureRepository.getConfigures(systemId).filter { it.type == ConfigType.DISPLAY.typeName }
    }

    fun getColorConfigures(systemId: Long): List<Configure> {
        return configureRepository.getConfigures(systemId).filter { it.type == ConfigType.COLOR.typeName }
    }

    fun create(config: Configure) {
        configureRepository.create(config)
    }

    fun update(id: String, config: Configure) {
        val nodeConfigure = Configure(id, config.systemId, config.type, config.key, config.value, config.order)
        configureRepository.update(nodeConfigure)
    }

    fun delete(id: String) {
        configureRepository.delete(id)
    }

    fun isDisplayNode(systemId: Long, nodeName: String): Boolean {
        if (nodeName.endsWith("Test")) return false

        val displayConfig = this.getDisPlayConfigures(systemId)
        val continueNodes = displayConfig.filter { it.value == "contain" }.map { it.key }
        val hiddenNodes = displayConfig.filter { it.value == "hidden" }.map { it.key }

        return (continueNodes.isEmpty() || continueNodes.any { nodeName.contains(it) }) && hiddenNodes.all { !nodeName.contains(it) }
    }

    fun getNodeRelatedColorConfigures(systemId: Long, nodePropertyName: String): List<Configure> {
        val colorConfigures = this.getColorConfigures(systemId)
        return colorConfigures.filter { nodePropertyName.contains(it.key) }
    }

    fun updateConfigsByType(systemId: Long, type: String, configs: List<Configure>) {
        configureRepository.deleteConfiguresByType(systemId, type)
        configs.forEach { it.systemId = systemId }
        configureRepository.batchCreateConfigures(configs)
    }
}
