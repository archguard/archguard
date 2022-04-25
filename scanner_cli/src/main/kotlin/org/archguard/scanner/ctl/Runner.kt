package org.archguard.scanner.ctl

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.enum
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.ctl.command.ScannerCommand
import org.archguard.scanner.ctl.loader.AnalyserDispatcher
import org.slf4j.LoggerFactory

// parse the cli inputs as the standard command (controller), build the context and dispatch to run
class Runner : CliktCommand(help = "scanner cli") {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val type: AnalyserType by argument().enum(ignoreCase = true)
    private val systemId: String by argument(help = "system id").default("0")
    private val serverUrl: String by argument(help = "the base url of the archguard api server")

    private val path: String by option(help = "the path of target project").default(".")
    private val language: String by option(
        help = "official supported language: Java, Kotlin, TypeScript, CSharp, Python, Golang. You can also input a json to define your own language analyser."
    ).default("Java")
    private val features: List<String> by option(help = "features: API_CALLS, DB. You can also input a json to define your own feature analyser.").multiple()

    /**
     *  --language=java
     *  --features=API,DB
     *
     *  --language='{identifier: "java", version: "1.0.0" ... }'
     *  --features=API
     *  --features='{identifier: "DB", version: "1.0.0" ... }'
     */
    override fun run() {
        val command = ScannerCommand(
            type, systemId, serverUrl, path, language,
            features.map {
                when {
                    it.startsWith("{") -> Json.decodeFromString<AnalyserSpec>(it)
                    else -> it
                }
            },
        )
        AnalyserDispatcher().dispatch(command)
    }
}
