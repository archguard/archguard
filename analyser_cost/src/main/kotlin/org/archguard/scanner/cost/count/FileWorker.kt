package org.archguard.scanner.cost.count

import java.io.File

var FileProcessJobWorkers = 4 * Runtime.getRuntime().availableProcessors()

fun process(filePath: String): List<FileJob> {
    val languageWorker = LanguageWorker()

    File(filePath).walk(FileWalkDirection.BOTTOM_UP)
        .filter { it.isFile }
        .map {
            FileJob(
                language = "Java",
                filename = it.name,
                extension = it.extension,
                location = it.absolutePath,
                symlocation = it.absolutePath,
                content = it.readBytes(),
                bytes = it.length(),
            )
        }
        .chunked(FileProcessJobWorkers)

    return emptyList()
}
