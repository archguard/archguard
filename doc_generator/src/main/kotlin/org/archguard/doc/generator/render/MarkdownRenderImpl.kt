package org.archguard.doc.generator.render

class MarkdownRenderImpl<T> : Render<T>() {
    override fun T.buildMetadata(content: T.() -> Unit) {

    }

    override fun T.buildLink(address: String, content: T.() -> Unit) {

    }

    override fun T.buildLineBreak() {


    }
}