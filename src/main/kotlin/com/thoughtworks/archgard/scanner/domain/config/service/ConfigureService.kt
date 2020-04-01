package com.thoughtworks.archgard.scanner.domain.config.service

import com.thoughtworks.archgard.scanner.domain.config.dto.UpdateDTO
import com.thoughtworks.archgard.scanner.domain.config.model.ToolConfigure
import com.thoughtworks.archgard.scanner.domain.config.repository.ConfigureRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ConfigureService {

    @Autowired
    private lateinit var configureRepository: ConfigureRepository

    fun getConfigures(): List<ToolConfigure> {
        return configureRepository.getConfigures()
    }

    fun updateConfigure(configs: List<ToolConfigure>): UpdateDTO {
        val result = configs.map { it.configs.map { i -> configureRepository.updateConfigure(it.type, i.key, i.value) } }.flatten().sumBy { it }
        return if (result > 0) {
            UpdateDTO(true, "Update config success")
        } else {
            UpdateDTO(false, "There is no such config refer to this id")
        }
    }

}