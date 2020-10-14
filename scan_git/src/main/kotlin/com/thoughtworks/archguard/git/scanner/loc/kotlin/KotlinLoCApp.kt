package com.thoughtworks.archguard.git.scanner.loc.kotlin

import com.thoughtworks.archguard.git.scanner.loc.LoCRepository
import com.thoughtworks.archguard.git.scanner.loc.ModuleUtil.Companion.getModule
import com.thoughtworks.archguard.git.scanner.loc.ProcessFiles
import com.thoughtworks.archguard.git.scanner.loc.model.JClassLoC
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.function.Consumer

class KotlinLoCApp(private val systemId: String, private val repository: LoCRepository) {
    fun analysisDir(dir: String) {
        val startingDir = Paths.get(dir)
        val fileAnalysis = parseKotlinFile()
        val pf = ProcessFiles(fileAnalysis)
        Files.walkFileTree(startingDir, pf)
    }

    private fun parseKotlinFile(): Consumer<Path> {
        return Consumer { path: Path ->
            try {
                if (path.toString().endsWith(".kt")) {
                    parse(path)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun parse(path: Path) {
        val locs = ArrayList<JClassLoC>()
        val visitor = KotlinLoCVisitor(locs, getModule(path))

        val tree = KotlinFileParser.parse(path)
        visitor.visit(tree)
        repository.save(locs, systemId)
    }
}