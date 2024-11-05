package org.archguard.scanner.ctl.loader

import chapi.domain.core.CodeDataStruct
import io.mockk.*
import org.archguard.scanner.core.Analyser
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context
import org.archguard.scanner.core.sourcecode.SourceCodeAnalyser
import org.archguard.scanner.ctl.client.ArchGuardConsoleClient
import org.archguard.scanner.ctl.command.ScannerCommand
import org.archguard.scanner.ctl.impl.CliSourceCodeContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class AnalyserDispatcherTest {
    @BeforeEach
    internal fun setUp() {
        mockkObject(AnalyserLoader)
    }

    @AfterEach
    internal fun tearDown() {
        clearAllMocks()
    }

    @Nested
    inner class ForSourceCodeContext {
        private val languageAnalyser = mockk<SourceCodeAnalyser>()
        private val protoAnalyser = mockk<SourceCodeAnalyser>()
        private val thriftAnalyser = mockk<SourceCodeAnalyser>()
        private val feature1Analyser = mockk<SourceCodeAnalyser>()
        private val feature2Analyser = mockk<SourceCodeAnalyser>()
        private val context = mockk<CliSourceCodeContext>()
        private val command = mockk<ScannerCommand>()

        private val fixture = ScannerCommand(
            type = AnalyserType.SOURCE_CODE,
            systemId = "0",
            serverUrl = "http://localhost:8080/",
            path = ".",
            output = listOf("json"),
        )

        private fun stubCommand() {
            every { command.type } returns AnalyserType.SOURCE_CODE
            every { command.path } returns "."
            every { command.language } returns "kotlin"
            every { command.features } returns listOf("feature1", "feature2")
            every { command.slots } returns listOf()
            every { command.buildClient() } returns fixture.copy().buildClient()
            every { command.getAnalyserSpec(any()) } returns mockk()
            every { command.withFunctionCode } returns false
            every { command.debug } returns false

            mockkConstructor(ArchGuardConsoleClient::class)
            every { anyConstructed<ArchGuardConsoleClient>().saveDataStructure(any()) } just runs
        }

        private fun stubContext() {
            every { context.language } returns "kotlin"
            every { context.features } returns listOf("feature1", "feature2")
        }

        private fun stubLoad() {
            every {
                AnalyserLoader.load(any(), any())
            } returns (languageAnalyser as Analyser<Context>) andThen (protoAnalyser as Analyser<Context>) andThen (thriftAnalyser as Analyser<Context>) andThen (feature1Analyser as Analyser<Context>) andThen (feature2Analyser as Analyser<Context>)
        }

        private fun mockAnalysers() {
            val ast = mockk<MutableList<CodeDataStruct>>()

            every { ast.isEmpty() } returns true
            every { ast.size } returns 0
            every { ast.addAll(any()) } returns true
            every { ast.iterator() } returns mutableListOf<CodeDataStruct>().iterator()

            every { languageAnalyser.analyse(null) } returns ast
            every { protoAnalyser.analyse(null) } returns ast
            every { thriftAnalyser.analyse(null) } returns ast
            every { feature1Analyser.analyse(ast) } returns null
            every { feature2Analyser.analyse(ast) } returns null
        }

        @Test
        fun `should dispatch to the source code analyser, pass the ast to following feature analyser`() {
            stubCommand()
            stubContext()
            stubLoad()
            mockAnalysers()

            AnalyserDispatcher().dispatch(command)
        }
    }
}
