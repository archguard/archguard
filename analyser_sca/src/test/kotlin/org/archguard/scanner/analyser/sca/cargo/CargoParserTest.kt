package org.archguard.scanner.analyser.sca.cargo;

import org.archguard.scanner.core.sca.DEP_SCOPE
import org.archguard.scanner.core.sca.DeclFileTree
import org.archguard.scanner.core.sca.DepSource
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CargoParserTest {

    @Test
    fun `lookupSource should return empty list when given file has no dependencies`() {
        // Given
        val cargoParser = CargoParser()
        val file = DeclFileTree(
            filename = "Cargo.toml",
            path = "/path/to/Cargo.toml",
            content = "[package]\nname = \"my-package\"\nversion = \"1.0.0\"\n",
            childrens = emptyList(),
            name = "Cargo.toml"
        )

        // When
        val result = cargoParser.lookupSource(file)

        // Then
        assertEquals(emptyList(), result)
    }

    @Test
    fun `lookupSource should return list of PackageDependencies when given file has dependencies`() {
        // Given
        val cargoParser = CargoParser()
        val file = DeclFileTree(
            filename = "Cargo.toml",
            path = "/path/to/Cargo.toml",
            content = """
                [dependencies]
                ort = { version = "2.0.0-alpha.1", default-features = true }
                tokenizers = { version = "0.15.0", default-features = false, features = ["progressbar", "cli", "onig", "esaxx_fast"] }
                ndarray = "0.15.6"
                enfer_core = { path = "../enfer_core" }
                
                [build-dependencies]
                uniffi = { version = "0.25", features = ["build"] }
            """.trimIndent(),
            childrens = emptyList(),
            name = "Cargo.toml"
        )

        // When
        val result = cargoParser.lookupSource(file).first().dependencies

        // Then
        assertEquals(5, result.size)
        assertEquals("ort", result[0].name)
        assertEquals("2.0.0-alpha.1", result[0].version)
        assertEquals("tokenizers", result[1].name)
        assertEquals("0.15.0", result[1].version)
        assertEquals("ndarray", result[2].name)
        assertEquals("0.15.6", result[2].version)
        assertEquals("enfer_core", result[3].name)
        assertEquals(DEP_SCOPE.BUILD, result[4].scope)
    }
}
