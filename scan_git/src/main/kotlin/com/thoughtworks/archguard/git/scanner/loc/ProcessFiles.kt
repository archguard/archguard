package com.thoughtworks.archguard.git.scanner.loc

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.function.Consumer

class ProcessFiles(private val process: Consumer<Path>) : SimpleFileVisitor<Path>() {

    // Print information about
    // each type of file.
    override fun visitFile(file: Path,
                           attr: BasicFileAttributes): FileVisitResult {

        if (attr.isRegularFile) {
            process.accept(file)
        }
        return FileVisitResult.CONTINUE
    }

    // Print each directory visited.
    override fun postVisitDirectory(dir: Path,
                                    exc: IOException?): FileVisitResult {
        return FileVisitResult.CONTINUE
    }

    // If there is some error accessing
    // the file, let the user know.
    // If you don't override this method
    // and an error occurs, an IOException
    // is thrown.
    override fun visitFileFailed(file: Path,
                                 exc: IOException?): FileVisitResult {
        System.err.println(exc)
        return FileVisitResult.CONTINUE
    }
}