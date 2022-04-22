package org.archguard.scanner.ctl

import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context
import org.archguard.scanner.ctl.client.ArchGuardDBClient
import org.archguard.scanner.ctl.impl.CliSourceCodeContext
import org.archguard.scanner.ctl.loader.AnalyserDispatcher

class Runner {
    fun execute(params: Map<String, Any>) {
        val context = buildContext(params)
        val customizedScannerSpecs =
            buildCustomizedAnalyserSpecs(params.getValue("specs") as? List<Map<String, String>>)

        AnalyserDispatcher(context, customizedScannerSpecs).dispatch()
    }

    private fun buildCustomizedAnalyserSpecs(params: List<Map<String, String>>?): List<AnalyserSpec> {
        return params?.map {
            AnalyserSpec(
                identifier = it.getValue("identifier"),
                host = it.getValue("host"),
                version = it.getValue("version"),
                className = it.getValue("className"),
                jar = it.getValue("jar"),
            )
        } ?: emptyList()
    }

    private fun buildContext(params: Map<String, Any>): Context {
        val type = (params["type"] as? String)?.let { AnalyserType.valueOf(it) }
            ?: throw  IllegalArgumentException("analyser type is not defined")

        return when (type) {
            AnalyserType.SOURCE_CODE -> {
                val language = params.getValue("language").toString()
                val systemId = params.getValue("systemId").toString()
                val path = params.getValue("path").toString()
                // val serverUrl = params.getValue("archguard_server_url").toString()
                CliSourceCodeContext(
                    systemId = systemId,
                    language = language,
                    features = params.getValue("features").toString().split(","),
                    client = buildClient(language, systemId, path),
                    path = path,
                    withoutStorage = params.getValue("systemId").toString().toBoolean(),
                )
            }
            else -> TODO("Not implemented yet")
        }
    }

    private fun buildClient(language: String, systemId: String, path: String) =
        ArchGuardDBClient(language, systemId, path)
}
