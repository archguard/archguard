package com.thoughtworks.archguard.git.scanner.loc

import com.thoughtworks.archguard.git.scanner.loc.model.JClassLoC
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Consumer

class JavaLoCApp(private val systemId: String) {
    private val repository = JavaLoCRepository()
    fun analysisDir(dir: String) {
        val startingDir = Paths.get(dir)
        val fileAnalysis = parseJavaFile()
        val pf = ProcessFiles(fileAnalysis)
        Files.walkFileTree(startingDir, pf)
        repository.toFile()
    }

    private fun parseJavaFile(): Consumer<Path> {
        return Consumer { path: Path ->
            try {
                if (path.toString().endsWith(".java")) {
                    parse(path)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun parse(path: Path) {
        val tree = JavaFileParser.parse(path)
        val loc = JClassLoC(module = getModule(path))
        val visitor = JavaLoCVisitor(loc)
        visitor.visit(tree)
        repository.save(loc, systemId)
    }

    private fun getModule(path: Path): String? {
        val file = path.toFile()
        var parentFile = file.parentFile
        var i = path.nameCount
        while (!(parentFile.list().orEmpty().toList()).contains("src") || i < 1) {
            parentFile = parentFile.parentFile
            if (parentFile == null) {
                return null
            }
            i--
        }
        return parentFile.name
    }
}