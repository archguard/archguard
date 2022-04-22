package org.archguard.scanner.ctl.loader

import chapi.domain.core.CodeDataStruct
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.archguard.scanner.core.Analyser
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.context.Context
import org.archguard.scanner.core.sourcecode.SourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.archguard.scanner.ctl.impl.OfficialAnalyserSpecs
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AnalyserDispatcherTest {
    private val context = mockk<SourceCodeContext> {
        every { language } returns "lang_kotlin"
        every { features } returns listOf("feature1", "feature2")
    }

    @BeforeEach
    internal fun setUp() {
        mockkObject(AnalyserLoader)
    }

    @AfterEach
    internal fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should dispatch to the official source code analyser, pass the ast to following feature analyser`() {
        val customized = listOf<AnalyserSpec>(
            mockk { every { identifier } returns "feature1" },
            mockk { every { identifier } returns "feature2" },
        )
        val languageAnalyser = mockk<SourceCodeAnalyser>()
        val feature1Analyser = mockk<SourceCodeAnalyser>()
        val feature2Analyser = mockk<SourceCodeAnalyser>()
        val ast = mockk<List<CodeDataStruct>>()
        every { languageAnalyser.analyse(null) } returns ast
        every { feature1Analyser.analyse(ast) } returns null
        every { feature2Analyser.analyse(ast) } returns null
        every {
            AnalyserLoader.load(context, any())
        } returns (languageAnalyser as Analyser<Context>) andThen (feature1Analyser as Analyser<Context>) andThen (feature2Analyser as Analyser<Context>)

        val dispatcher = AnalyserDispatcher(context, customized)
        dispatcher.dispatch()

        verify {
            AnalyserLoader.load(context, OfficialAnalyserSpecs.LANG_KOTLIN.spec())
        }
    }

    @Test
    fun `should dispatch to the customized source code analyser`() {
        val customizedLanguageAnalyser = mockk<AnalyserSpec> { every { identifier } returns "lang_kotlin" }
        val customized = listOf<AnalyserSpec>(
            customizedLanguageAnalyser,
            mockk { every { identifier } returns "feature1" },
            mockk { every { identifier } returns "feature2" },
        )
        val languageAnalyser = mockk<SourceCodeAnalyser>()
        val feature1Analyser = mockk<SourceCodeAnalyser>()
        val feature2Analyser = mockk<SourceCodeAnalyser>()
        val ast = mockk<List<CodeDataStruct>>()
        every { languageAnalyser.analyse(null) } returns ast
        every { feature1Analyser.analyse(ast) } returns null
        every { feature2Analyser.analyse(ast) } returns null
        every {
            AnalyserLoader.load(context, any())
        } returns (languageAnalyser as Analyser<Context>) andThen (feature1Analyser as Analyser<Context>) andThen (feature2Analyser as Analyser<Context>)

        val dispatcher = AnalyserDispatcher(context, customized)
        dispatcher.dispatch()

        verify {
            AnalyserLoader.load(context, customizedLanguageAnalyser)
        }
    }
}
