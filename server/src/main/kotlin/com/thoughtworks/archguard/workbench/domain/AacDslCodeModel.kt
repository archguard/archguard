package com.thoughtworks.archguard.workbench.domain


data class AacDslCodeModel(
    val id: Long? = null,
    val title: String = "",
    val author: String = "",
    val description: String = "",
    val content: String,
)