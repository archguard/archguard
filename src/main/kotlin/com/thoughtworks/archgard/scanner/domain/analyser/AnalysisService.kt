package com.thoughtworks.archgard.scanner.domain.analyser

import com.thoughtworks.archgard.scanner.domain.system.SystemInfo
import com.thoughtworks.archgard.scanner.domain.system.SystemInfoRepository
import com.thoughtworks.archgard.scanner.domain.system.SystemOperator
import com.thoughtworks.archgard.scanner.domain.exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths

@Service
class AnalysisService(@Autowired val systemInfoRepository: SystemInfoRepository) {
    fun getSystemOperator(id: Long): SystemOperator {
        val systemInfo = systemInfoRepository.getSystemInfo(id)
                ?: throw EntityNotFoundException(SystemInfo::class.java, id)
        checkAnalysable(systemInfo)
        return SystemOperator(systemInfo, id)
    }

    fun checkAnalysable(systemInfo: SystemInfo) {
        if (systemInfo.repoType == "ZIP") {
            systemInfo.getRepoList().forEach {
                if (!Files.exists(Paths.get(it))) {
                    throw FileNotFoundException("zip file has been deleted: $it")
                }
            }
        }
    }
}
