package com.thoughtworks.archgard.scanner.domain.config.service

import com.thoughtworks.archgard.scanner.domain.config.dto.ConfigureDTO
import com.thoughtworks.archgard.scanner.domain.config.dto.UpdateDTO
import com.thoughtworks.archgard.scanner.domain.config.dto.UpdateMessageDTO
import com.thoughtworks.archgard.scanner.domain.config.repository.ConfigureRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ConfigureService {

    @Autowired
    private lateinit var configureRepository: ConfigureRepository

    fun getConfigures(): List<ConfigureDTO> {
        return configureRepository.getConfigures()
    }

    fun updateConfigure(configs: List<UpdateDTO>): UpdateMessageDTO {
        val result = configs.map { configureRepository.updateConfigure(it.id, it.value) }.sumBy { it }
        return if (result > 0) {
            UpdateMessageDTO(true, "Update config success")
        } else {
            UpdateMessageDTO(false, "There is no such config refer to this id")
        }
    }

}