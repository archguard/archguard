package com.thoughtworks.archgard.scanner.domain.tbs

data class TestBadSmell(
        val id: String,
        val line: Int,
        val fileName: String,
        val description: String,
        val type: String)
