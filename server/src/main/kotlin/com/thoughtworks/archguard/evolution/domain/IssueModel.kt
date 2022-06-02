package com.thoughtworks.archguard.evolution.domain

import org.jdbi.v3.core.mapper.reflect.JdbiConstructor


data class IssueModel
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