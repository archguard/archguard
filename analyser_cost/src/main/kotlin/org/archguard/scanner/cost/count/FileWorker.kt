package org.archguard.scanner.cost.count

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

fun processByDir(filePath: String): List<LanguageSummary> {
    val files = File(filePath).walk(FileWalkDirection.BOTTOM_UP)
        .filter {
            it.isFile
                    && !it.absolutePath.contains(".git")
                    && !it.absolutePath.contains("node_modules")
                    && !it.absolutePath.contains("build")
        }
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
        return@runBlocking process(files).toList()
    }
}

suspend fun process(files: List<FileJob>) = coroutineScope {
    val channel = Channel<FileJob?>()
    launch {
        files.forEach {
            channel.send(LanguageWorker().processFile(it))
        }

        channel.close()
    }

    return@coroutineScope fileSummarizeLong(channel)
}

suspend fun CoroutineScope.fileSummarizeLong(channel: Channel<FileJob?>): MutableList<LanguageSummary> {
    val languages = mutableMapOf<String, LanguageSummary>()
    var sumLines: Long = 0
    var sumBlank: Long = 0
    var sumComment: Long = 0
    var sumCode: Long = 0
    var sumComplexity: Long = 0
    var sumWeightedComplexity: Double = 0.0
    var sumFiles: Long = 0
    var sumBytes: Long = 0
    for (res in channel) {
        if (res == null) continue

        sumLines += res.lines
        sumBlank += res.blank
        sumComment += res.comment
        sumCode += res.code
        sumComplexity += res.complexity
        sumWeightedComplexity += res.weightedComplexity
        sumFiles++
        sumBytes += res.bytes

        var weightedComplexity = 0.0
        if (res.code != 0L) {
            weightedComplexity = res.complexity.toDouble() / res.code * 100
        }
        res.weightedComplexity = weightedComplexity
        sumWeightedComplexity += weightedComplexity

        if (languages[res.language] == null) {
            val files = mutableListOf(res)
            languages[res.language] = LanguageSummary(
                name = res.language,
                lines = res.lines,
                code = res.code,
                comment = res.comment,
                blank = res.blank,
                complexity = res.complexity,
                count = 1,
                weightedComplexity = weightedComplexity,
                files = files,
            )
        } else {
            val tmp = languages[res.language]!!
            val files = tmp.files.toMutableList()
            files.add(res)
            languages[res.language] = LanguageSummary(
                name = res.language,
                lines = tmp.lines + res.lines,
                code = tmp.code + res.code,
                comment = tmp.comment + res.comment,
                blank = tmp.blank + res.blank,
                complexity = tmp.complexity + res.complexity,
                count = tmp.count + 1,
                weightedComplexity = tmp.weightedComplexity + weightedComplexity,
                files = files,
            )
        }
    }
    val language = mutableListOf<LanguageSummary>()
    for (summary in languages) {
        language.add(summary.value)
    }

    return language
}