package com.thoughtworks.archguard.git.scanner

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class LanguageServiceTest {

    @Test
    fun should_return_LICENSE_when_is_a_license_file() {
        val lang = LanguageService()
        assertEquals("License", lang.detectLanguage("LICENSE")[0])
    }

    @Test
    fun should_check_gitignore() {
        val lang = LanguageService()
        assertEquals("gitignore", lang.detectLanguage(".gitignore")[0])
    }

    @Test
    fun should_return_typescript_define_file() {
        val lang = LanguageService()
        assertEquals("TypeScript Typings", lang.detectLanguage("types.d.ts")[0])
    }

    @Test
    fun should_test_get_extension_no_extension() {
        val lang = LanguageService()
        assertEquals("YAML", lang.detectLanguage(".travis.yml")[0])
    }

    @Test
    fun should_return_ext_name() {
        val lang = LanguageService()
        assertEquals("d.ts", lang.getExtension("types.d.ts"))
    }
}