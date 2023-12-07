package org.archguard.scanner.analyser

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.archguard.scanner.analyser.count.FileJob
import org.archguard.scanner.analyser.count.LanguageWorker
import org.archguard.scanner.analyser.ignore.Gitignore
import org.archguard.scanner.analyser.ignore.IgnoreMatcher
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

    private fun readDir(path: String): Array<out File>? {
        val file = File(path)
        if (!file.exists()) {
            throw Exception("failed to open $path")
        }
        if (!file.isDirectory) {
            throw Exception("failed to read $path")
        }

        return file.listFiles()
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

    private suspend fun walk(workdir: String) = coroutineScope {
        val dirents = readDir(workdir) ?: return@coroutineScope

        dirents.map {
            val name = it.name

            if (name == ".gitignore" || it.name == ".ignore") {
                val path = File(workdir).resolve(name).toString()
                val file = Gitignore.create(path) ?: return@map
                ignores.add(file)
            }
        }

        dirents.forEach { file ->
            val name = file.name
            val path = file.toString()
            val isDir = file.isDirectory


            for (deny in PathDenyList) {
                if (path.contains(deny)) {
                    return@forEach
                }
            }

            for (exclude in excludes) {
                if (exclude.matches(path) || exclude.matches(name)) {
                    return@forEach
                }
            }

            for (ignore in ignores) {
                if (ignore.match(path, isDir)) {
                    return@forEach
                }
            }

            if (!isDir) {
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
