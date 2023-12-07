package org.archguard.scanner.analyser.count

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.archguard.scanner.analyser.DirectoryWalker

/**
 * The FileWorker class provides a set of static methods for working with files.
 *
 * This class contains a single method, start(), which allows for the processing and summarization
 * of files in a specified directory.
 *
 * The start() method uses coroutines to perform the following steps:
 * 1. Reads the specified directory and collects the files into a collection of FileJob objects.
 * 2. Adds language information to each FileJob object by calling the LanguageWorker's processFile() method.
 * 3. Summarizes the language details by computing various metrics such as lines of code, comments, blank lines,
 *    complexity, and weighted complexity.
 *
 * The fileSummarizeLong() method is a private method used internally by the start() method to calculate the language
 * summary based on the FileJob objects received from the channel.
 *
 * This class is designed to be used in conjunction with the LanguageWorker and DirectoryWalker classes to provide
 * a comprehensive analysis of files in a specified directory.
 *
 * Usage:
 * ```
 * val filePath = "/path/to/directory"
 * val summary = FileWorker.start(filePath)
 * ```
 */
class FileWorker {
    companion object {
        suspend fun start(filePath: String) = coroutineScope {
            val walkdirChannel = Channel<FileJob>()
            val langDetailChannel = Channel<FileJob>()

            val languageWorker = LanguageWorker()
            var summary = mutableListOf<LanguageSummary>()

            // 1. read directory to collection of files for FileJob
            // 2. add language information to FileJob
            // 3. add summary information to LanguageSummary
            launch {
                launch {
                    DirectoryWalker(walkdirChannel).start(filePath)

                    walkdirChannel.close()
                }
                launch {
                    for (fileJob in walkdirChannel) {
                        languageWorker.processFile(fileJob)?.let {
                            langDetailChannel.send(it)
                        }
                    }

                    langDetailChannel.close()
                }
                launch {
                    summary = fileSummarizeLong(langDetailChannel)
                }
            }.join()

            return@coroutineScope summary
        }

        private suspend fun fileSummarizeLong(channel: Channel<FileJob>): MutableList<LanguageSummary> {
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
    }
}