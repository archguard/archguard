package com.thoughtworks.archgard.scanner.domain.config.repository

import com.thoughtworks.archgard.scanner.domain.config.dto.ConfigureDTO
import com.thoughtworks.archgard.scanner.domain.config.model.ToolConfigure

interface ConfigureRepository {

    fun getConfigures(): List<ConfigureDTO>

    fun updateConfigure(id: String, value: String): Int

    fun cleanRegistered(configs: List<String>)

    fun register(scanners: List<String>)

    fun getRegistered(): List<String>

    fun ifAvailable(type: String): Boolean

    fun getToolConfigures(): List<ToolConfigure>
}