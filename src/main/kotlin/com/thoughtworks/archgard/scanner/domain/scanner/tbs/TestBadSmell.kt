package com.thoughtworks.archgard.scanner.domain.scanner.tbs

data class TestBadSmell(
        val id: String,
        val systemId: Long,
        val line: Int,
        val fileName: String,
        val description: String,
        val type: String)
