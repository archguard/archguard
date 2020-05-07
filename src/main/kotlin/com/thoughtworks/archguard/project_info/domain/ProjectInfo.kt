package com.thoughtworks.archguard.project_info.domain

data class ProjectInfo(val id: String?, val projectName: String, val gitRepo: List<String>, val sql: String?)