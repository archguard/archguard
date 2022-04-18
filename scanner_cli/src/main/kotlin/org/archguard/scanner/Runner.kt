package org.archguard.scanner

import chapi.domain.core.CodeDataStruct
import org.archguard.scanner.client.ArchGuardHttpClient
import org.archguard.scanner.core.LanguageScanner
import org.archguard.scanner.core.context.Context
import org.archguard.scanner.core.context.ScannerSpec
import org.archguard.scanner.impl.SourceCodeContext
import org.archguard.scanner.loader.ScannerExecutor

// 作为探针, 根据 archguard 的调用参数创建不同的context
// context包含了scanner执行过程中所需的上下文信息, 还有数据存储的回调方法等
// executor启动后, 会根据上下文信息拉取指定的scanner类, 并按照树状结构执行

// 官方实现的scanner会提前写入official_scanner_specs
// 自定义实现的scanner需使用已有的context, 实现LanguageScanner or FeatureScanner
// 将其打包后, 通过spec参数通过上述流程自动加载
// 自动加载通过下载jar, 读取外部class进行执行
fun main() {
    val params = mapOf(
        "scanner" to "source_code",
        "language" to "java",
        "features" to "api, mysql",
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

    val context: Context = SourceCodeContext(
        language = params.getValue("language").toString(),
        features = params.getValue("features").toString().split(","),
        path = params.getValue("path").toString(),
        systemId = params.getValue("systemId").toString(),
        withoutStorage = params.getValue("systemId").toString().toBoolean(),
        client = ArchGuardHttpClient(params.getValue("archguard_server_url").toString()),
    )
    val customizedScannerSpecs = (params.getValue("specs") as List<Map<String, String>>).map {
        ScannerSpec(
            identifier = "identifier",
            host = "host",
            version = "version",
            jar = "jar",
        )
    }

    // execute
    ScannerExecutor(context, customizedScannerSpecs).execute()
}

class JavaScanner(override val context: Context) : LanguageScanner<SourceCodeContext> {
    override fun execute(): List<CodeDataStruct> {
        return emptyList()
    }
}
