package org.archguard.scanner.cost.count

import kotlinx.serialization.Serializable
import java.security.MessageDigest

typealias LineType = Int

typealias FileJobCallback = (job: FileJob, currentLine: Long, lineType: LineType) -> Boolean

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
    @kotlinx.serialization.Transient
    var hash: MessageDigest = MessageDigest.getInstance("SHA-256"),
    var callback: FileJobCallback? = null,
    var binary: Boolean = false,
    var minified: Boolean = false,
    var generated: Boolean = false,
    var endPoint: Int = 0,
)
