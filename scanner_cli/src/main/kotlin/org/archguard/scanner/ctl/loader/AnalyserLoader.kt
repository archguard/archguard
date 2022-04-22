package org.archguard.scanner.ctl.loader

import org.archguard.scanner.core.Analyser
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.context.Context
import org.slf4j.LoggerFactory
import java.net.URLClassLoader
import java.nio.file.Path
import java.nio.file.Paths

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
        TODO("download from remote url")
    }

    private fun AnalyserSpec.copyFrom() {
        val sourceJar = Paths.get(host).resolve(jar)
        val targetJar = getLocalPath().toFile()

        logger.debug("analyser is configured as absolute path, copying to installation path...")
        logger.debug("| $sourceJar -> $targetJar |")

        sourceJar.toFile().copyTo(targetJar)
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

        return spec.run {
            val jarUrl = getLocalPath().toUri().toURL()
            val cl = URLClassLoader(arrayOf(jarUrl), this.javaClass.classLoader)
            Class.forName(spec.getFullClassName(), true, cl)
                .declaredConstructors[0]
                .newInstance(context) as Analyser<Context>
        }
    }
}
