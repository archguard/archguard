package org.archguard.scanner.estimate.count

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.security.MessageDigest

@Serializable
class FileJob(
    var language: String = "",
    var possibleLanguages: List<String> = listOf(),
    var filename: String = "",
    var extension: String = "",
    var location: String = "",
    var symlocation: String = "",
    var content: ByteArray = byteArrayOf(),
    var bytes: Long = 0,
    var lines: Long = 0,
    var code: Long = 0,
    var comment: Long = 0,
    var blank: Long = 0,
    var complexity: Long = 0,
    var weightedComplexity: Double = 0.0,
    // skip serialisation
    @Transient
    var hash: MessageDigest = MessageDigest.getInstance("SHA-256"),
    var binary: Boolean = false,
    var minified: Boolean = false,
    var generated: Boolean = false,
    var endPoint: Int = 0,
)
