package org.archguard.scanner.tbs

import chapi.app.analyser.ChapiAnalyser
import chapi.app.analyser.config.ChapiConfig
import kotlinx.serialization.Serializable
import org.archguard.linter.rule.testcode.TestSmellProvider
import org.archguard.linter.rule.testcode.TestSmellVisitor

@Serializable
open class TestBadSmell(
    var fileName: String = "",
    var type: String = "",
    var description: String = "",
    var line: Int = 0
)

class TbsAnalyser(
    private val config: ChapiConfig = ChapiConfig()
) {
    fun analysisByPath(path: String): Array<TestBadSmell> {
        val nodes = ChapiAnalyser(config).analysis(path)

        val provider = TestSmellProvider()
        val visitor = TestSmellVisitor(nodes)
        return nodes.flatMap { ds ->
            visitor
                .visitor(listOf(provider.get()), ds)
                .map {
                    TestBadSmell(
                        fileName = ds.FilePath,
                        type = it.name,
                        description = it.detail,
                        line = it.position.startLine
                    )
                }
        }.toTypedArray()
    }
}
