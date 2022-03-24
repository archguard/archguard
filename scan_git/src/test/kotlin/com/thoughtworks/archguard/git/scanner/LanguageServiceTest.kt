package com.thoughtworks.archguard.git.scanner

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class LanguageServiceTest {

    @Test
    fun should_return_LICENSE_when_is_a_license_file() {
        val lang = LanguageService()
        assertEquals("License", lang.detectLanguage("LICENSE"))
    }

    @Test
    fun should_check_gitignore() {
        val lang = LanguageService()
        assertEquals("gitignore", lang.detectLanguage(".gitignore"))
    }

//    @Test
//    fun should_return_typescript_define_file() {
//        val lang = LanguageService()
//        assertEquals("TypeScript Typings", lang.detectLanguage("d.ts"))
//    }
}