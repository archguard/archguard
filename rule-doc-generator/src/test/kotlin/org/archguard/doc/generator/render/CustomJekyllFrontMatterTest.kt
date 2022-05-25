package org.archguard.doc.generator.render

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CustomJekyllFrontMatterTest {
    @Test
    internal fun head_to_text() {
        val fm = CustomJekyllFrontMatter(title = "SQL", navOrder = 1, permalink = "sql")
        assertEquals("""---
layout: default
title: SQL
parent: Governance
nav_order: 1
permalink: /governance/sql
---
""", fm.toMarkdown())
    }
}