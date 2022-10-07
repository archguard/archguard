package com.thoughtworks.archguard.systeminfo.controller

import com.thoughtworks.archguard.systeminfo.domain.RepoAuthType
import com.thoughtworks.archguard.systeminfo.domain.ScannedType

data class SystemInfoDTO(
    var id: Long? = null,
    val systemName: String = "",
    val repo: List<String> = ArrayList(),
    val sql: String = "",
    val username: String = "",
    val password: String = "",
    val scanned: ScannedType = ScannedType.NONE,
    val qualityGateProfileId: Long? = null,
    val repoType: String = "GIT",
    val updatedTime: Long? = null,
    val badSmellThresholdSuiteId: Long? = null,
    val branch: String = "master",
    val language: String = "java",
    val codePath: String = "",
    val repoAuthType: RepoAuthType = RepoAuthType.UsernameAndPassword,
    val sshKeyString: String? = ""
)
