package com.thoughtworks.archguard.systeminfo.domain

import com.thoughtworks.archguard.scanner.infrastructure.AESCrypt
import java.sql.Timestamp

class SystemInfo(
    val id: Long? = null,
    val systemName: String,
    val repo: String,
    val username: String,
    val password: String,
    var scanned: ScannedType = ScannedType.NONE,
    @Deprecated("not used")
    val qualityGateProfileId: Long? = null,
    val repoType: String,
    val updatedTime: Timestamp? = null,
    val badSmellThresholdSuiteId: Long?,
    val branch: String,
    val language: String,
    // for TypeScript/JavaScript, if some code is in subdiretory
    val codePath: String,
    // git clone target directory
    val workdir: String = "",
    @Deprecated("not used")
    val repoAuthType: RepoAuthType = RepoAuthType.UsernameAndPassword,
    @Deprecated("not used")
    val sshKeyString: String? = ""

) {
    fun getDeCryptPassword(): String = AESCrypt.decrypt(password)

    fun hasAuthInfo(): Boolean {
        return username.isNotBlank() && password.isNotBlank()
    }

    fun getRepoList(): List<String> = repo.split(",")
}

enum class ScannedType {
    NONE, SCANNING, SCANNED, FAILED
}

enum class RepoAuthType {
    SshKeyString,
    UsernameAndPassword
}
