package org.archguard.scanner.cost

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
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
    private var root: String = ""
    private val ignores: MutableList<IgnoreMatcher> = mutableListOf()
    private val dirChannels = mutableListOf<Channel<DirectoryJob>>()

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

    suspend fun start(workdir: String) = coroutineScope {
        root = workdir

        val file = File(workdir)
        if (!file.exists()) throw Exception("failed to open $workdir")


        if (!file.isDirectory) {
            launch {
                LanguageWorker.createFileJob(file).let {
                    output.send(it)
                }
            }
        } else {
            createDirJob(workdir, workdir)
        }
    }

    // dynamic channel for dir
    private fun CoroutineScope.createDirJob(root: String, path: String) {
        val channel = Channel<DirectoryJob>(1)
        dirChannels.add(channel)

        launch {
            channel.send(DirectoryJob(root, path, ignores))

            for (dir in channel) {
                walk(path)
            }

            channel.close()
        }
    }

    private suspend fun walk(path: String) = coroutineScope {
        val dirents = readDir(path) ?: return@coroutineScope

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
                    launch {
                        output.send(it)
                    }
                }
            } else {
                createDirJob(root, file.absolutePath)
            }
        }

        if(dirChannels.size >= 1) {
            dirChannels.removeAt(0).close()
        }
    }
}
