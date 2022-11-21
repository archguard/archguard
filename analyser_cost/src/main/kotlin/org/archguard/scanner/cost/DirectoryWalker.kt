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

// refactor: change to coroutine
class DirectoryWalker(
    val output: Channel<FileJob>,
    val excludes: List<Regex>
) {

    // // Readdir reads a directory such that we know what files are in there
    //func (dw *DirectoryWalker) Readdir(path string) ([]os.FileInfo, error) {
    //	file, err := os.Open(path)
    //	if err != nil {
    //		return []os.FileInfo{}, fmt.Errorf("failed to open %s: %v", path, err)
    //	}
    //	defer file.Close()
    //
    //	dirents, err := file.Readdir(-1)
    //	if err != nil {
    //		return []os.FileInfo{}, fmt.Errorf("failed to read %s: %v", path, err)
    //	}
    //
    //	return dirents, nil
    //}
    fun readdir(path: String): List<File> {
        val file = File(path)
        if (!file.exists()) {
            throw Exception("failed to open $path")
        }
        return file.listFiles()!!.toList()
    }


    companion object {
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

        fun walk(path: String): List<File>? {
            val ignores: MutableList<IgnoreMatcher> = mutableListOf()

            val dirents = readDir(path) ?: return null

            dirents.map {
                if (it.name == ".gitignore" || it.name == ".ignore") {
                    val file = Gitignore.create(it.absolutePath) ?: return@map
                    ignores.add(file)
                }
            }

            println("Found ${ignores.size} ignore files")

            // filter path by ignore files
            val filtered = dirents.filter {
                !ignores.any { ignore -> ignore.match(it.absolutePath, it.isDirectory) }
            }

            return filtered
        }
    }
}