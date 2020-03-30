package com.thoughtworks.archgard.scanner.domain.config.service

import com.thoughtworks.archgard.scanner.domain.config.model.ScannerConfigure
import com.thoughtworks.archgard.scanner.domain.config.dto.AddDTO
import com.thoughtworks.archgard.scanner.domain.config.dto.ConfigureDTO
import com.thoughtworks.archgard.scanner.domain.config.dto.DeleteDTO
import com.thoughtworks.archgard.scanner.domain.config.dto.UpdateDTO
import com.thoughtworks.archgard.scanner.domain.config.repository.ConfigureRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ConfigureService {

    @Autowired
    private lateinit var configureRepository: ConfigureRepository

    fun addConfigure(config: ConfigureDTO): AddDTO {
        val id = configureRepository.addConfigure(config)
        return AddDTO(true, "Add a new config", id)
    }

    fun getConfigures(): List<ScannerConfigure> {
        return configureRepository.getConfigures()
    }

    fun deleteConfigure(id: String): DeleteDTO {
        val result = configureRepository.deleteConfigure(id)
        return if (result >= 1) {
            DeleteDTO(true, "Delete config success")
        } else {
            DeleteDTO(false, "There is no such configure refer to this id")
        }
    }

    fun updateConfigure(config: ConfigureDTO): UpdateDTO {
        val result = configureRepository.updateConfigure(config.id, config.type, config.key, config.value)
        return if (result > 0) {
            UpdateDTO(true, "Update config success")
        } else {
            UpdateDTO(false, "There is no such config refer to this id")
        }
    }

}