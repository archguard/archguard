package org.archguard.scanner.ctl

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.enum
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.long
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.ctl.command.ScannerCommand
import org.archguard.scanner.ctl.loader.AnalyserDispatcher
import org.slf4j.LoggerFactory

/**
 * parse the cli inputs as the standard command (controller), build the context and dispatch to run
 **/
class Runner : CliktCommand(help = "scanner cli") {
    private val logger = LoggerFactory.getLogger(javaClass)

    // cli parameters
    private val type by option().enum<AnalyserType>(ignoreCase = true).default(AnalyserType.SOURCE_CODE)
    private val systemId by option(help = "system id").default("0")
    private val serverUrl by option(help = "the base url of the archguard api server").default("http://localhost:8080")
    private val workspace by option(help = "the workspace directory").default(".")
    private val path by option(help = "the path of target project").default(".")
    private val output by option(help = "http, csv, json, console").multiple()

    // additional parameters
    private val analyserSpec by option(help = "Override the analysers via json.").multiple()
    private val slotSpec by option(help = "Override the slot via json.").multiple()
    private val language by option(help = "language: Java, Kotlin, TypeScript, CSharp, Python, Golang.")

    // TODO refactor as DAG (analyser - dependencies[analyser, analyser])
    private val features by option(help = "features: apicalls, datamap.").multiple()
    private val repoId by option(help = "repository id used for git analysing")
    private val branch by option(help = "repository branch").default("master")
    private val startedAt by option(help = "TIMESTAMP, the start date of the scanned commit").long().default(0L)
    private val since by option(help = "COMMIT ID, the specific revision of the baseline")
    private val until by option(help = "COMMIT ID, the specific revision of the target")
    private val depth by option(help = "INTEGER, the max loop depth").int().default(7)

    /**
     *  --language=java
     *  --features=API,DB
     *  --analyserSpec='{identifier: "java", version: "1.0.0" ... }'
     *  --analyserSpec='{identifier: "DB", version: "1.0.0" ... }'
     **/
    override fun run() {
        logger.debug(
            """
            <cli parameters>
            |type: $type
            |systemId: $systemId
            |serverUrl: $serverUrl
            |workspace: $workspace
            |path: $path
            |output: $output
            <customized analysers>
            |analyzerSpec: $analyserSpec
            |slotSpec: $slotSpec
            <additional parameters>
            |language: $language
            |features: $features
            |repoId: $repoId
            |branch: $branch
            |startedAt: $startedAt
            |since: $since
            |until: $until
            |depth: $depth
           """.trimIndent()
        )

        val command = ScannerCommand(
            // cli parameters
            type, systemId, serverUrl, path, output, analyserSpec.map { Json.decodeFromString(it) },
            // additional parameters
            language?.lowercase(), features.map { it.lowercase() }, repoId, branch, startedAt, since, until, depth,
            slotSpec.map { Json.decodeFromString(it) },
        )
        AnalyserDispatcher().dispatch(command)
    }
}

fun main(args: Array<String>) = Runner().main(args)
