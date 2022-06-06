package com.thoughtworks.archguard.issue

import org.jdbi.v3.core.mapper.reflect.JdbiConstructor


data class EvoIssueModel
@JdbiConstructor
constructor(
    val ruleId: String,
    val position: String,
    val name: String,
    val detail: String,
    val ruleType: String,
    val severity: String,
    val fullName: String = "",
    val source: String = "",
)