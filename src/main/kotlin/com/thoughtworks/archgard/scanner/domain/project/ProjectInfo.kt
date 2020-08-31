package com.thoughtworks.archgard.scanner.domain.project

import com.thoughtworks.archgard.scanner.infrastructure.AESCrypt

class ProjectInfo(var id: Long? = null,
                  val systemName: String = "",
                  val repo: String = "",
                  val sql: String = "",
                  val username: String = "",
                  val password: String = "",
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
