package org.archguard.scanner.ctl.loader

import io.mockk.every
import io.mockk.mockk
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.archguard.scanner.ctl.loader.AnalyserLoader.installPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import kotlin.reflect.full.memberFunctions

internal class AnalyserLoaderTest {
    private val fakeJarName = "testonly-lang_kotlin-1.6.1-all.jar"
    private val spec = AnalyserSpec(
        identifier = "lang_kotlin",
        host = "<unknown>",
        version = "1.6.1",
        jar = fakeJarName,
        className = "MyApiAnalyser",
    )
    private val context = mockk<SourceCodeContext> {
        every { client } returns mockk()
    }

    private fun fakeInstall(source: File) = source.copyTo(installPath.resolve(spec.jar).toFile(), overwrite = true)
    private fun fakeUninstall() = installPath.resolve(spec.jar).deleteIfExists()

    @AfterEach
    internal fun tearDown() {
        fakeUninstall()
    }

    @Test
    fun `should load the analyser from remote jar via http url`() {
        // skip for Windows
        if (System.getProperty("os.name").lowercase().contains("win")) return

        val url = "https://github.com/archguard/archguard/raw/master/scanner_cli/src/test/resources/kotlin"
        val analyser = AnalyserLoader.load(context, spec.copy(host = url))

        val kClass = analyser::class
        assertThat(kClass.simpleName).isEqualTo("MyApiAnalyser")
        assertThat(kClass.memberFunctions.map { it.name }).contains("analyse")
    }

    @Test
    fun `should load the analyser from local jar via absolute path`() {
        if (System.getProperty("os.name").lowercase().contains("win")) return

        val folder = this.javaClass.classLoader.getResource("kotlin")!!
        val analyser = AnalyserLoader.load(context, spec.copy(host = folder.path))

        val kClass = analyser::class
        assertThat(kClass.simpleName).isEqualTo("MyApiAnalyser")
        assertThat(kClass.memberFunctions.map { it.name }).contains("analyse")
    }

    @Test
    fun `should load the analyser from existing jar`() {
        if (System.getProperty("os.name").lowercase().contains("win")) return

        val folder = this.javaClass.classLoader.getResource("kotlin")!!
        fakeInstall(Path(folder.path).resolve(fakeJarName).toFile())
        val analyser = AnalyserLoader.load(context, spec)

        val kClass = analyser::class
        assertThat(kClass.simpleName).isEqualTo("MyApiAnalyser")
        assertThat(kClass.memberFunctions.map { it.name }).contains("analyse")
    }
}
