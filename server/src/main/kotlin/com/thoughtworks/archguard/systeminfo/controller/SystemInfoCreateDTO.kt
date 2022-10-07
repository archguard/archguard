package com.thoughtworks.archguard.systeminfo.controller

data class SystemInfoCreateDTO(
    val systemName: String = "",
    val repo: List<String> = ArrayList(),
    val username: String = "",
    val password: String = "",
    val repoType: String = "GIT",
    val updatedTime: Long?,
    val badSmellThresholdSuiteId: Long?,
    val branch: String = "master",
    val language: String = "java",
    val codePath: String = "",
)
