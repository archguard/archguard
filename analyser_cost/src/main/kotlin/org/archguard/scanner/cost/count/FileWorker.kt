package org.archguard.scanner.cost.count

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

var FileProcessJobWorkers = 4 * Runtime.getRuntime().availableProcessors()

fun process(filePath: String): List<FileJob> {
    val languageWorker = LanguageWorker()

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

    runBlocking {
        process(languageWorker, files)
    }

    return emptyList()
}

suspend fun process(languageWorker: LanguageWorker, files: List<FileJob>) = coroutineScope {
    val channel = Channel<FileJob?>()
    launch {
        files.forEach {
            println("Sending ${it.filename}")
            channel.send(languageWorker.processFile(it))
        }

        channel.close()
    }

    launch {
        // todo: summary it here
        for (element in channel) {
            println(element?.location)
        }
    }
}