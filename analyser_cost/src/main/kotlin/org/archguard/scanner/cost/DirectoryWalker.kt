package org.archguard.scanner.cost

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.archguard.scanner.cost.count.FileJob
import org.archguard.scanner.cost.ignore.Gitignore
import org.archguard.scanner.cost.ignore.IgnoreMatcher
import java.io.File


class DirectoryJob(
    val root: String,
    val path: String,
    val ignores: List<IgnoreMatcher>
)

// refactor: change to coroutine
class DirectoryWalker(
    val output: Channel<FileJob>,
    val excludes: List<Regex> = listOf()
) {
    // Readdir reads a directory such that we know what files are in there
    fun readDir(path: String): Array<out File>? {
        val file = File(path)
        if (!file.exists()) {
            throw Exception("failed to open $path")
        }
        if (!file.isDirectory) {
            throw Exception("failed to read $path")
        }

        return file.listFiles()
    }

    suspend fun walk(path: String) = coroutineScope {
        val ignores: MutableList<IgnoreMatcher> = mutableListOf()

        val dirents = readDir(path) ?: return@coroutineScope

        dirents.map {
            if (it.name == ".gitignore" || it.name == ".ignore") {
                val file = Gitignore.create(it.absolutePath) ?: return@map
                ignores.add(file)
            }
        }

        launch {
            dirents.forEach { file ->
                for (ignore in ignores) {
                    if (ignore.match(file.absolutePath, file.isDirectory)) {
                        return@forEach
                    }
                }

                for (exclude in excludes) {
                    if (exclude.matches(file.absolutePath)) {
                        return@forEach
                    }
                }

                if (!file.isDirectory) {
                    output.send(
                        FileJob(
                            language = "Java",
                            filename = file.name,
                            extension = file.extension,
                            location = file.absolutePath,
                            symlocation = file.absolutePath,
                            content = file.readBytes(),
                            bytes = file.length(),
                        )
                    )
                }
            }
        }
    }
}