package org.archguard.analyser.sca.parser

import org.archguard.analyser.sca.model.DEP_SCOPE
import org.archguard.analyser.sca.model.DeclFileTree
import org.archguard.analyser.sca.model.PackageDependencies
import org.archguard.analyser.sca.model.DependencyEntry
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class MavenParser : Parser() {
    override fun lookupSource(file: DeclFileTree): List<PackageDependencies> {
        val builderFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val builder: DocumentBuilder = builderFactory.newDocumentBuilder()
        val xmlDocument: Document = builder.parse(InputSource(StringReader(file.content)))
        val xPath: XPath = XPathFactory.newInstance().newXPath()

        val groupId = xPath.compile("/project/groupId").evaluate(xmlDocument, XPathConstants.STRING)
        val artifactId = xPath.compile("/project/artifactId").evaluate(xmlDocument, XPathConstants.STRING)
        val version = xPath.compile("/project/version").evaluate(xmlDocument, XPathConstants.STRING)

        return listOf(
            PackageDependencies(
                name = "$groupId:$artifactId",
                version = "$version",
                packageManager = "maven",
                dependencies = lookupDependencies(xPath, xmlDocument)
            )
        )
    }

    private fun lookupDependencies(xPath: XPath, xmlDocument: Document): List<DependencyEntry> {
        val expression = "/project/dependencies/dependency"
        val nodeList = xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET) as NodeList

        return (0 until nodeList.length).map { i ->
            val item: Element = nodeList.item(i) as Element
            val groupId = xPath.evaluate("groupId", item) ?: ""
            val artifact = xPath.evaluate("artifactId", item) ?: ""
            val scope = xPath.evaluate("scope", item) ?: ""

            DependencyEntry(
                name = "$groupId:$artifact",
                group = groupId,
                artifact = artifact,
                version = xPath.evaluate("version", item) ?: "",
                scope = DEP_SCOPE.from(scope)
            )
        }.toList()
    }
}