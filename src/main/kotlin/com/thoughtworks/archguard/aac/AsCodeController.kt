package com.thoughtworks.archguard.aac

import com.thoughtworks.archguard.common.exception.DuplicateResourceException
import com.thoughtworks.archguard.system_info.controller.SystemInfoDTO
import com.thoughtworks.archguard.system_info.controller.SystemInfoMapper
import com.thoughtworks.archguard.system_info.domain.SystemInfoService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

class AsCodeRepositoryDTO(val name: String, val language: String, val scmUrl: String)

class RepoStatus(val success: List<String>, val failure: List<String>)

@RestController
@RequestMapping("/api/ascode")
class AsCodeController(val systemInfoService: SystemInfoService, val systemInfoMapper: SystemInfoMapper) {
    private val logger = LoggerFactory.getLogger(AsCodeController::class.java)

    @PutMapping("/repos")
    fun createRepos(@RequestBody repos: List<AsCodeRepositoryDTO>): RepoStatus {
        val successName = mutableListOf<String>()
        val failureName = mutableListOf<String>()

        repos.forEach {
            val systemInfoDTO = SystemInfoDTO(systemName = it.name, repo = listOf(it.scmUrl), language = it.language)
            val systemInfo = systemInfoMapper.fromDTO(systemInfoDTO)
            try {
                systemInfoService.addSystemInfo(systemInfo)
                successName += it.name
            } catch (e: Exception) {
                failureName += it.name
            }
        }

        return RepoStatus(successName, failureName)
    }
}
