package org.archguard.scanner.cost.count

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

//var FileProcessJobWorkers = 4 * Runtime.getRuntime().availableProcessors()

fun process(filePath: String): LanguageSummary {
    val languageWorker = LanguageWorker()

    // todo: filter ignore files ?
    val files = File(filePath).walk(FileWalkDirection.BOTTOM_UP)
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
        .toList()

    return runBlocking {
        return@runBlocking process(languageWorker, files)
    }
}

suspend fun process(languageWorker: LanguageWorker, files: List<FileJob>) = coroutineScope {
    val channel = Channel<FileJob?>()
    launch {
        files.forEach {
            channel.send(languageWorker.processFile(it))
        }

        channel.close()
    }


    val languageSummary = LanguageSummary()
    launch {
        for (fileJob in channel) {
            if (fileJob != null) {
                fileSummary(fileJob, languageSummary)
            }
        }
    }

    return@coroutineScope languageSummary
}

fun fileSummary(element: FileJob, languageSummary: LanguageSummary) {
    languageSummary.files += element
    languageSummary.lines += element.lines
    languageSummary.code += element.code
    languageSummary.comment += element.comment
    languageSummary.blank += element.blank
    languageSummary.complexity += element.complexity
    languageSummary.weightedComplexity += element.weightedComplexity
    languageSummary.count += 1
    languageSummary.bytes += element.bytes
}
