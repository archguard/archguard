package org.archguard.scanner.analyser

import org.archguard.scanner.core.openapi.ApiCollection
import org.archguard.scanner.core.openapi.OpenApiContext
import java.nio.file.Path
import kotlin.io.path.*

class OpenApiAnalyser(override val context: OpenApiContext) : org.archguard.scanner.core.openapi.OpenApiAnalyser {
    private val client = context.client
    private val path = context.path

    @OptIn(ExperimentalPathApi::class)
    override fun analyse(): List<ApiCollection> {
        val targetsFile: MutableList<Path> = mutableListOf()
        val target = Path(path)

        if (target.isDirectory()) {
            targetsFile += target.walk().filter {
                !it.toString().contains("src/main/resources") && mayBeOasFile(it)
            }.toList()
        } else {
            if (mayBeOasFile(target)) {
                targetsFile.add(target)
            }
        }

        val collections = targetsFile.mapNotNull {
            ApiProcessorDetector.detectApiProcessor(it.toFile(), withPostman = true)
        }.map {
            it.convertApi()
        }.flatten()

        client.saveOpenApi(collections)

        return collections
    }

    private fun mayBeOasFile(it: Path) = it.extension == "json" || it.extension == "yaml" || it.extension == "yml"
}