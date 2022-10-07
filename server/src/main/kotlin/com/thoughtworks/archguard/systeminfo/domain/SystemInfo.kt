package com.thoughtworks.archguard.systeminfo.domain

import java.sql.Timestamp

data class SystemInfo(
    var id: Long? = null,
    val systemName: String = "",
    val repo: String = "",
    @Deprecated("not used")
    val sql: String = "",
    @Deprecated("not used")
    val username: String = "",
    @Deprecated("not used")
    val password: String = "",
    val scanned: ScannedType = ScannedType.NONE,
    @Deprecated("not used")
    val qualityGateProfileId: Long? = null,
    @Deprecated("not used")
    val repoType: String = "GIT",
    val updatedTime: Timestamp? = null,
    @Deprecated("not used")
    val badSmellThresholdSuiteId: Long? = null,
    val branch: String? = "master",
    val language: String? = "java",
    // for TypeScript/JavaScript, if some code is in subdiretory
    val codePath: String? = "",
    // git clone target directory
    val workdir: String? = "",
    @Deprecated("not used")
    val repoAuthType: RepoAuthType = RepoAuthType.UsernameAndPassword,
    @Deprecated("not used")
    val sshKeyString: String? = ""
)

enum class ScannedType {
    NONE, SCANNING, SCANNED, FAILED
}

enum class RepoAuthType {
    SshKeyString,
    UsernameAndPassword
}
