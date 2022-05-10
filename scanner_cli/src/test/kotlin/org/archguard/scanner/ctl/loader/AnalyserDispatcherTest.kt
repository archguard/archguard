package org.archguard.scanner.ctl.loader

import chapi.domain.core.CodeDataStruct
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.archguard.scanner.core.Analyser
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context
import org.archguard.scanner.core.sourcecode.SourceCodeAnalyser
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
        private val feature1Analyser = mockk<SourceCodeAnalyser>()
        private val feature2Analyser = mockk<SourceCodeAnalyser>()
        private val context = mockk<CliSourceCodeContext>()
        private val command = mockk<ScannerCommand>()

        private fun stubCommand() {
            every { command.type } returns AnalyserType.SOURCE_CODE
            every { command.languageSpec } returns mockk { every { identifier } returns "kotlin" }
            every { command.featureSpecs } returns listOf(
                mockk { every { identifier } returns "feature1" },
                mockk { every { identifier } returns "feature2" },
            )
            every { command.path } returns "."
            every { command.buildClient() } returns mockk()
        }

        private fun stubContext() {
            every { context.language } returns "kotlin"
            every { context.features } returns listOf("feature1", "feature2")
        }

        private fun stubLoad() {
            every {
                AnalyserLoader.load(any(), any())
            } returns (languageAnalyser as Analyser<Context>) andThen (feature1Analyser as Analyser<Context>) andThen (feature2Analyser as Analyser<Context>)
        }

        private fun mockAnalysers() {
            val ast = mockk<List<CodeDataStruct>>()
            every { languageAnalyser.analyse(null) } returns ast
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
