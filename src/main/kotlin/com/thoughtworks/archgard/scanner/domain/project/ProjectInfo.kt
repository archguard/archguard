package com.thoughtworks.archgard.scanner.domain.project

import com.thoughtworks.archgard.scanner.infrastructure.AESCrypt

class ProjectInfo(var id: String = "",
                  val projectName: String = "",
                  val repo: String = "",
                  val sql: String = "",
                  val username: String = "",
                  val password: String = "",
                  val repoType: String = "GIT") {
    fun getDeCryptPassword(): String = AESCrypt.decrypt(password)
}
