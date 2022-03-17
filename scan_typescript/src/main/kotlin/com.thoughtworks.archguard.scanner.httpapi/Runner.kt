package com.thoughtworks.archguard.scanner.httpapi

import chapi.app.analyser.TypeScriptAnalyserApp
import chapi.app.frontend.ComponentHttpCallInfo
import chapi.app.frontend.FrontendApiAnalyser
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import java.io.File
import java.util.*

class Runner : CliktCommand(help = "scan git to sql") {
    private val systemId: String by option(help = "system id").default("0")
    private val path: String by option(help = "local path").default(".")

    override fun run() {
        val path = File(path).absolutePath
        val nodes = TypeScriptAnalyserApp().analysisNodeByPath(path)
        val componentCalls: Array<ComponentHttpCallInfo> = FrontendApiAnalyser().analysis(nodes, path)

        val file = File("apis.sql")
        if (file.exists()) {
            file.delete()
        }

        val bean2Sql = Bean2Sql()
        componentCalls.map { api ->
            var ids = arrayOf<String>()
            api.apiRef.map {
                val demandId = UUID.randomUUID().toString()
                ids += demandId
                val str = bean2Sql.bean2Sql(
                    Demand(
                        id = demandId,
                        httpMethod = it.method,
                        targetUrl = it.url,
                        caller = it.caller
                    )
                )

                file.appendText("$str\n")
            }

            val serviceStr = ServiceApi(
                systemId = systemId.toLong(),
                demandIds = ids.joinToString(","),
                resourceIds = ""
            )
            file.appendText("${bean2Sql.bean2Sql(serviceStr)}\n")
        }
    }
}

fun main(args: Array<String>) = Runner().main(args)

