package com.thoughtworks.archguard.systeminfo.controller.dto

import com.thoughtworks.archguard.systeminfo.domain.ScannedType
import kotlinx.serialization.Serializable

@Serializable
data class SystemInfoDTO(
    val id: Long?,
    val systemName: String,
    val repo: List<String> = ArrayList(),
    val username: String,
    val password: String,
    val scanned: ScannedType,
    val repoType: String,
    val updatedTime: Long?,
    val badSmellThresholdSuiteId: Long?,
    val branch: String,
    val language: String,
    val codePath: String,
)
