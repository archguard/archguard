package org.archguard.scanner.cost.count

typealias LineType = Int

typealias FileJobCallback = (job: FileJob, currentLine: Long, lineType: LineType) -> Boolean

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
    var hash: String = "",
    var callback: FileJobCallback? = null,
    var binary: Boolean = false,
    var minified: Boolean = false,
    var generated: Boolean = false,
    var endPoint: Int = 0,
)
