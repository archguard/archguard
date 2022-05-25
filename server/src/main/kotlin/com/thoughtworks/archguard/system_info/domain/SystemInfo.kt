package com.thoughtworks.archguard.system_info.domain

import com.thoughtworks.archguard.system_info.controller.RepoAuthType
import java.sql.Timestamp

data class SystemInfo(
    var id: Long? = null,
    val systemName: String = "",
    val repo: String = "",
    val sql: String = "",
    val username: String = "",
    val password: String = "",
    val scanned: ScannedType = ScannedType.NONE,
    val qualityGateProfileId: Long? = null,
    val repoType: String = "GIT",
    val updatedTime: Timestamp? = null,
    val badSmellThresholdSuiteId: Long? = null,
    val branch: String? = "master",
    val language: String? = "java",
    // for TypeScript/JavaScript, if some code is in subdiretory
    val codePath: String? = "",
    // git clone target directory
    val workdir: String? = "",
    val repoAuthType: RepoAuthType = RepoAuthType.UsernameAndPassword,
    val sshKeyString: String? = ""
)
