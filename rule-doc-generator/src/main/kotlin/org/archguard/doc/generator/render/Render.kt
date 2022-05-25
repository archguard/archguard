package org.archguard.doc.generator.render

abstract class Render<T> {
    abstract fun T.buildMetadata(content: T.() -> Unit)
    abstract fun T.buildLink(address: String, content: T.() -> Unit)
    abstract fun T.buildLineBreak()
}

interface ContentNode

data class DocPage(var content: List<ContentNode>) : ContentNode
data class DocHeader(val title: String, val content: List<ContentNode>, val level: Int) : ContentNode
data class DocText(val text: String) : ContentNode
data class DocBreakLine(val text: String = "") : ContentNode
data class DocCodeBlock(val language: String, val text: String) : ContentNode

data class CustomJekyllFrontMatter(
    val layout: String = "default",
    var title: String,
    val parent: String = "Governance",
    val navOrder: Int,
    val permalink: String,
) : ContentNode {

    fun toMarkdown(): String {
        return """
---
layout: $layout
title: $title
parent: $parent
nav_order: $navOrder
permalink: /governance/$permalink
---

""".trimIndent()
    }
}