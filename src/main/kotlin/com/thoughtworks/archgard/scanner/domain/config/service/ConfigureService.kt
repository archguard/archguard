package com.thoughtworks.archgard.scanner.domain.config.service

import com.thoughtworks.archgard.scanner.domain.config.dto.ConfigureDTO
import com.thoughtworks.archgard.scanner.domain.config.dto.UpdateDTO
import com.thoughtworks.archgard.scanner.domain.config.model.ScannerConfigure
import com.thoughtworks.archgard.scanner.domain.config.repository.ConfigureRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ConfigureService {

    @Autowired
    private lateinit var configureRepository: ConfigureRepository

    fun addConfigure(configs: List<ConfigureDTO>): List<String> =
            configs.map { configureRepository.addConfigure(it) }

    fun getConfigures(): List<ScannerConfigure> {
        return configureRepository.getConfigures()
    }

    fun updateConfigure(configs: List<ConfigureDTO>): UpdateDTO {
        val result = configs.map { configureRepository.updateConfigure(it.id, it.type, it.key, it.value) }.sumBy { it }
        return if (result == configs.size) {
            UpdateDTO(true, "Update config success")
        } else {
            UpdateDTO(false, "There is no such config refer to this id")
        }
    }

}