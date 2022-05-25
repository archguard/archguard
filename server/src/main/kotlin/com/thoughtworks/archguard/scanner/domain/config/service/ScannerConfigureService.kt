package com.thoughtworks.archguard.scanner.domain.config.service

import com.thoughtworks.archguard.scanner.domain.config.dto.ConfigureDTO
import com.thoughtworks.archguard.scanner.domain.config.dto.UpdateDTO
import com.thoughtworks.archguard.scanner.domain.config.dto.UpdateMessageDTO
import com.thoughtworks.archguard.scanner.domain.config.repository.ScannerConfigureRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ScannerConfigureService {

    @Autowired
    private lateinit var configureRepository: ScannerConfigureRepository

    fun getConfigures(): List<ConfigureDTO> {
        return configureRepository.getConfigures()
    }

    fun updateConfigure(configs: List<UpdateDTO>): UpdateMessageDTO {
        val result = configs.map { configureRepository.updateConfigure(it.id, it.value) }.sumOf { it }
        return if (result > 0) {
            UpdateMessageDTO(true, "Update config success")
        } else {
            UpdateMessageDTO(false, "There is no such config refer to this id")
        }
    }
}
