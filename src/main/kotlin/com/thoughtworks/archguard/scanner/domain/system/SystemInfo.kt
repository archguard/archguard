package com.thoughtworks.archguard.scanner.domain.system

import com.thoughtworks.archguard.scanner.infrastructure.AESCrypt

class SystemInfo(var id: Long? = null,
                 val systemName: String = "",
                 val repo: String = "",
                 val sql: String = "",
                 val username: String = "",
                 val password: String = "",
                 val language: String = "jvm",
                 var scanned: ScannedType = ScannedType.NONE,
                 val repoType: String = "GIT") {
    fun getDeCryptPassword(): String = AESCrypt.decrypt(password)

    fun hasAuthInfo(): Boolean {
        return username.isNotBlank() && password.isNotBlank()
    }

    fun getRepoList(): List<String> = repo.split(",")

    fun isNecessaryBuild(): Boolean {
        return repoType != "ZIP"
    }
}
