package org.archguard.scanner.cost

import kotlinx.coroutines.channels.Channel
import org.archguard.scanner.cost.count.FileJob
import org.archguard.scanner.cost.ignore.Gitignore
import org.archguard.scanner.cost.ignore.IgnoreMatcher
import java.io.File


class DirectoryJob(
    val root: String,
    val path: String,
    val ignores: List<IgnoreMatcher>
)

class DirectoryWalker(
    val output: Channel<FileJob>,
    val excludes: List<Regex>
) {
    init {

    }

    companion object {
        fun walk(path: String) {
            val ignores: MutableList<IgnoreMatcher> = mutableListOf()

            File(path).listFiles()?.map {
                if (it.name == ".gitignore" || it.name == ".ignore") {
                    ignores += (Gitignore.create(it.absolutePath))
                }
            }

            println("Found ${ignores.size} ignore files")
        }
    }
}