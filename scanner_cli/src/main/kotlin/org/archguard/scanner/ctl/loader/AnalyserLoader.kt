package org.archguard.scanner.ctl.loader

import org.archguard.scanner.core.Analyser
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.context.Context
import org.slf4j.LoggerFactory
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.system.measureTimeMillis

object AnalyserLoader {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val rootPath = Paths.get("").toAbsolutePath()
    private const val folder = "dependencies/analysers"

    val installPath: Path = rootPath.resolve(folder)

    private fun AnalyserSpec.isInstalled(): Boolean {
        logger.debug("workspace path: $rootPath")
        logger.debug("analyser install path: $installPath")

        return (!installPath.toFile().listFiles { _, name -> name == jar }.isNullOrEmpty()).also {
            if (it) logger.debug("analyser: $identifier - [$version] is installed")
            else logger.debug("analyser: $identifier - [$version] is not installed")
        }
    }

    private fun AnalyserSpec.install() {
        when {
            // TODO fix windows
            host.startsWith("/") -> this.copyFrom()
            host.startsWith("http") -> this.download()
            else -> throw IllegalArgumentException("please use absolute path or http url to install the analyser")
        }
    }

    private fun AnalyserSpec.download() {
        val sourceUrl = URL("$host/$jar")
        val targetJarPath = getLocalPath()

        logger.debug("analyser is configured as url, downloading to installation path...")
        logger.debug("| $sourceUrl -> $targetJarPath |")

        logger.debug("downloading...")
        val cost = measureTimeMillis {
            sourceUrl.openStream().use {
                Files.copy(it, targetJarPath)
            }
        }
        logger.debug("downloading finished in $cost ms")
    }

    private fun AnalyserSpec.copyFrom() {
        val sourceJarFile = Paths.get(host).resolve(jar).toFile()
        val targetJarFile = getLocalPath().toFile()

        logger.debug("analyser is configured as absolute path, copying to installation path...")
        logger.debug("| $sourceJarFile -> $targetJarFile |")

        sourceJarFile.copyTo(targetJarFile)
    }

    private fun AnalyserSpec.getFullClassName(): String {
        if (className.contains(".")) return className

        return "org.archguard.scanner.analyser.$className"
    }

    private fun AnalyserSpec.getLocalPath(): Path {
        return rootPath.resolve(folder).resolve(jar)
    }

    fun load(context: Context, spec: AnalyserSpec): Analyser<Context> {
        if (!spec.isInstalled()) spec.install()
        val jarUrl = spec.getLocalPath().toUri().toURL()
        val cl = URLClassLoader(arrayOf(jarUrl), this.javaClass.classLoader)

        return Class.forName(spec.getFullClassName(), true, cl)
            .declaredConstructors[0]
            .newInstance(context) as Analyser<Context>
    }
}
