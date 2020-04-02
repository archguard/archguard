package com.thoughtworks.archgard.scanner.domain.config.repository

import com.thoughtworks.archgard.scanner.domain.config.model.ToolConfigure
import com.thoughtworks.archgard.scanner.domain.config.dto.ConfigureDTO

interface ConfigureRepository {

    fun getConfigures(): List<ToolConfigure>

    fun updateConfigure(type: String, key: String, value: String): Int

    fun cleanRegistered(configs: List<String>)

    fun register(scanners: List<String>)

    fun getRegistered(): List<String>

    fun ifAvailable(type: String): Boolean
}