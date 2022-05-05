package org.archguard.scanner.bytecode.module

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collectors

object ModuleUtil {
    private val pomPattern = Pattern.compile(".*<artifactId>.*</artifactId>")
    private val gradlePattern = Pattern.compile(".*dependency.*\".*:.*:.*")

    @Throws(NoModuleException::class, IOException::class)
    fun getModule(path: Path): CodeModule {
        val file = path.toFile()
        var parentFile = file.parentFile
        var i = path.nameCount
        while (!listOf(*Objects.requireNonNull(getParentFileList(parentFile))).contains("src") || i < 1) {
            parentFile = parentFile.parentFile
            i--
        }
        val name = parentFile.name
        val modulePath = parentFile.path
        return CodeModule(name, modulePath, getModuleDependencies(parentFile))
    }

    @Throws(IOException::class)
    private fun getModuleDependencies(parentFile: File): Set<String> {
        val pomPath = Paths.get(parentFile.path, "pom.xml")
        if (pomPath.toFile().isFile) {
            return Files.lines(pomPath).map { input: String? -> pomPattern.matcher(input) }
                .filter { obj: Matcher -> obj.matches() }
                .map { m: Matcher -> m.group(0) }
                .map { c: String ->
                    c.split("<artifactId>").toTypedArray()[1]
                        .split("</artifactId>").toTypedArray()[0]
                }
                .collect(Collectors.toSet())
        }
        val gradlePath = Paths.get(parentFile.path, "build.gradle")
        return if (gradlePath.toFile().isFile) {
            Files.lines(gradlePath).map { input: String? -> gradlePattern.matcher(input) }
                .filter { obj: Matcher -> obj.matches() }
                .map { m: Matcher -> m.group(0) }
                .map { c: String -> c.split(":").toTypedArray()[1] }.collect(Collectors.toSet())
        } else HashSet()
    }

    @Throws(NoModuleException::class)
    private fun getParentFileList(parentFile: File?): Array<String> {
        if (parentFile == null) {
            throw NoModuleException()
        }
        return parentFile.list() ?: throw NoModuleException()
    }
}