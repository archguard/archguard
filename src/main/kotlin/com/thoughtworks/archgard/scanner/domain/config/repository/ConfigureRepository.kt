package com.thoughtworks.archgard.scanner.domain.config.repository

import com.thoughtworks.archgard.scanner.domain.config.model.ScannerConfigure
import com.thoughtworks.archgard.scanner.domain.config.dto.ConfigureDTO

interface ConfigureRepository {
    fun addConfigure(config: ConfigureDTO): String

    fun getConfigures(): List<ScannerConfigure>

    fun updateConfigure(id: String, type: String?, key: String?, value: String?): Int

    fun register(scanners: List<String>)

    fun getRegistered(): List<ScannerConfigure>

    fun cleanRegistered(configs: List<String>)
}