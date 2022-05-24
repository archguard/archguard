package com.thoughtworks.archguard.aac

import com.thoughtworks.archguard.aac.model.AsCodeResponse
import com.thoughtworks.archguard.aac.model.RepoStatus
import com.thoughtworks.archguard.system_info.controller.SystemInfoDTO
import com.thoughtworks.archguard.system_info.controller.SystemInfoMapper
import com.thoughtworks.archguard.system_info.domain.SystemInfoService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

class AsCodeRepositoryDTO(val name: String, val language: String, val scmUrl: String)

// todo: thinking in move to websocket?
@RestController
@RequestMapping("/api/ascode")
class AsCodeController(val systemInfoService: SystemInfoService, val systemInfoMapper: SystemInfoMapper) {
    private val logger = LoggerFactory.getLogger(AsCodeController::class.java)

    @PutMapping("/repos")
    fun createRepos(@RequestBody repos: List<AsCodeRepositoryDTO>): AsCodeResponse {
        val successes = mutableListOf<String>()
        val exists = mutableListOf<String>()

        repos.forEach {
            val systemInfoDTO = SystemInfoDTO(
                systemName = it.name,
                repo = listOf(it.scmUrl),
                language = it.language,
                qualityGateProfileId = 1
            )
            val systemInfo = systemInfoMapper.fromDTO(systemInfoDTO)
            try {
                systemInfoService.addSystemInfo(systemInfo)
                successes += it.name
            } catch (e: Exception) {
                exists += it.name
            }
        }

        return AsCodeResponse(
            content = RepoStatus(successes, exists)
        )
    }
}
