package org.archguard.scanner.ctl

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.context.Context
import org.archguard.scanner.core.sourcecode.ASTSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.LanguageSourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.archguard.scanner.ctl.client.ArchGuardHttpClient
import org.archguard.scanner.ctl.impl.CliSourceCodeContext
import org.archguard.scanner.ctl.loader.AnalyserDispatcher

// cli main entry
fun main() {
    val params = mapOf(
        "type" to "source_code",
        "language" to "java",
        "features" to "db",
        "path" to "kotlin/org/archguard/scanner/Runner.kt",
        "systemId" to "1",
        "withoutStorage" to "false",
        "archguard_server_url" to "http://localhost:8080/api/v1/",
        "specs" to listOf(
            mapOf(
                "name" to "customized_scanner",
                "url" to "https://raw.githubusercontent.com/archguard/archguard-scanner/master/official_scanner_specs.json"
            )
        )
    )

    // create source code context when type is source_code
    val context: Context = CliSourceCodeContext(
        language = params.getValue("language").toString(),
        features = params.getValue("features").toString().split(","),
        path = params.getValue("path").toString(),
        systemId = params.getValue("systemId").toString(),
        withoutStorage = params.getValue("systemId").toString().toBoolean(),
        client = ArchGuardHttpClient(params.getValue("archguard_server_url").toString()),
    )
    val customizedScannerSpecs = (params.getValue("specs") as List<Map<String, String>>).map {
        AnalyserSpec(
            identifier = "db",
            host = "host",
            version = "version",
            jar = "jar",
            className = "DBAnalyser",
        )
    }

    // execute
    AnalyserDispatcher(context, customizedScannerSpecs).dispatch()
}

// move to scan_xxx

class JavaAnalyser(override val context: SourceCodeContext) : LanguageSourceCodeAnalyser {
    override fun analyse(): List<CodeDataStruct> {
        println("im in java analyser")
        println("language: ${context.language}")
        println("features: ${context.features}")
        return listOf(CodeDataStruct(NodeName = "java"))
    }
}

class DBAnalyser(override val context: SourceCodeContext) : ASTSourceCodeAnalyser {
    override fun analyse(input: List<CodeDataStruct>): Any? {
        println("im in db analyser")
        println("input: ${input.map { it.NodeName }}")
        return null
    }
}
