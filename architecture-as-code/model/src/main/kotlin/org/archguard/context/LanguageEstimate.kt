package org.archguard.context

import kotlinx.serialization.Serializable

@Serializable
data class LanguageEstimate(
    // cost part
    val cost: Float,
    val month: Float,
    val people: Float,
    // language summary
    var name: String = "",
    var files: Long = 0,
    var lines: Long = 0,
    var blanks: Long = 0,
    var comment: Long = 0,
    var code: Long = 0,
    var complexity: Long = 0,
)
