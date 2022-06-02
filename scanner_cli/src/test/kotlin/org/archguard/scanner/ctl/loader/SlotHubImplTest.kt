package org.archguard.scanner.ctl.loader

import chapi.domain.core.CodeDataStruct
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.archguard.meta.Slot
import org.archguard.scanner.core.Analyser
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.client.ArchGuardClient
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context
import org.archguard.scanner.core.sourcecode.SourceCodeAnalyser
import org.archguard.scanner.ctl.command.ScannerCommand
import org.archguard.scanner.ctl.impl.CliSourceCodeContext
import org.archguard.scanner.ctl.impl.OfficialAnalyserSpecs
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class SlotHubImplTest {
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
        private val spec = mockk<SourceCodeAnalyser>()
        private val slot = mockk<Slot>()
        private val context = mockk<CliSourceCodeContext>()
        private val command = mockk<ScannerCommand>()
        private val client = mockk<ArchGuardClient>()

        private fun stubCommand() {
            every { command.type } returns AnalyserType.SOURCE_CODE
            every { command.path } returns "."
            every { command.language } returns "kotlin"
            every { command.features } returns listOf("feature1", "feature2")
            every { command.slots } returns listOf()
            every { command.buildClient() } returns mockk()
            every { command.getAnalyserSpec(any()) } returns mockk()
        }

        private fun stubContext() {
            every { context.language } returns "kotlin"
            every { context.client } returns client
        }

        private fun stubLoad() {
            every {
                AnalyserLoader.load(any(), any())
            } returns (languageAnalyser as Analyser<Context>) andThen (spec as Analyser<Context>)

            every {
                AnalyserLoader.loadSlot(any())
            } returns slot
        }

        private fun mockAnalysers() {
            val ast = mockk<List<CodeDataStruct>>()

            every { ast.isEmpty() } returns true

            every { languageAnalyser.analyse(null) } returns ast
            every { spec.analyse(ast) } returns null

            every { slot.ticket() } returns listOf("chapi.domain.core.CodeDataStruct")
            every { slot.prepare(any()) } returns listOf()
            every { slot.process(any()) } returns listOf()
        }

        @Test
        fun `load slot `() {
            stubCommand()
            stubContext()
            stubLoad()
            mockAnalysers()

            val jarFileName = "rule-webapi-" + OfficialAnalyserSpecs.Rule.version() + "-all.jar"
            val customized = AnalyserSpec(
                "rule-webapi",
                "",
                "",
                jarFileName,
                "org.archguard.scanner.core.Analyser",
                "notrule"
            )

            val slotHubImpl = SlotHubImpl(context)
            slotHubImpl.register(listOf(customized))

            val codeDataStruct = CodeDataStruct()
            slotHubImpl.maybePlugSlot(listOf(codeDataStruct))
        }
    }
}