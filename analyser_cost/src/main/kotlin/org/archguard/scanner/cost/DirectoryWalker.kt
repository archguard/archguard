package org.archguard.scanner.cost

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.archguard.scanner.cost.count.FileJob
import org.archguard.scanner.cost.count.LanguageWorker
import org.archguard.scanner.cost.ignore.Gitignore
import org.archguard.scanner.cost.ignore.IgnoreMatcher
import java.io.File


class DirectoryJob(
    val root: String,
    val path: String,
    val ignores: List<IgnoreMatcher>
)

// ".git", ".hg", ".svn"
val PathDenyList: List<String> = listOf(".git", ".hg", ".svn")

class DirectoryWalker(
    val output: Channel<FileJob>,
    val excludes: List<Regex> = listOf()
) {

    private var dirChannel: Channel<DirectoryJob> = Channel()

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

    suspend fun start(root: String) = coroutineScope {
        println("start processing: $root")
        val file = File(root)
        if (!file.exists()) throw Exception("failed to open $root")


        if (!file.isDirectory) {
            runBlocking {
                LanguageWorker.createFileJob(file).let {
                    output.send(it)
                }
            }
        } else {
            runBlocking {
                println("sending root: $root")
                dirChannel.send(DirectoryJob(root, root, listOf()))
            }
        }

        launch {
            for (directoryJob in dirChannel) {
                walk(directoryJob.path)
            }

            dirChannel.close()
        }
    }

    private suspend fun walk(path: String) {
        val ignores: MutableList<IgnoreMatcher> = mutableListOf()
        val dirents = readDir(path) ?: return

        dirents.map {
            if (it.name == ".gitignore" || it.name == ".ignore") {
                val file = Gitignore.create(it.absolutePath) ?: return@map
                ignores.add(file)
            }
        }

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

            for (deny in PathDenyList) {
                if (file.absolutePath.contains(deny)) {
                    return@forEach
                }
            }

            if (!file.isDirectory) {
                LanguageWorker.createFileJob(file).let {
                    runBlocking {
                        println("send file ${it.filename}")
                        output.send(it)
                    }
                }
            } else {
                runBlocking {
                    println("send dir: ${file.absolutePath}")
                    dirChannel.send(DirectoryJob(path, file.absolutePath, ignores))
                }
            }
        }
    }
}
