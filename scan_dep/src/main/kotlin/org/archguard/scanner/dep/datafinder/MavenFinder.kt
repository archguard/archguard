package org.archguard.scanner.dep.datafinder

import org.archguard.scanner.dep.common.Finder
import org.archguard.scanner.dep.model.DeclFile
import org.archguard.scanner.dep.model.DepDecl
import org.archguard.scanner.dep.model.DepDependency
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

class MavenFinder : Finder() {
    override fun lookupSource(file: DeclFile): List<DepDecl> {
        val builderFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val builder: DocumentBuilder = builderFactory.newDocumentBuilder()
        val xmlDocument: Document = builder.parse(InputSource(StringReader(file.content)))
        val xPath: XPath = XPathFactory.newInstance().newXPath()

        val groupId = xPath.compile("/project/groupId").evaluate(xmlDocument, XPathConstants.STRING)
        val artifactId = xPath.compile("/project/artifactId").evaluate(xmlDocument, XPathConstants.STRING)
        val version = xPath.compile("/project/version").evaluate(xmlDocument, XPathConstants.STRING)


        return listOf(
            DepDecl(
                name = "$groupId:$artifactId",
                version = "$version",
                packageManager = "maven",
                dependencies = lookupDependencies(xPath, xmlDocument)
            )
        )
    }

    private fun lookupDependencies(xPath: XPath, xmlDocument: Document): List<DepDependency> {
        val expression = "/project/dependencies/dependency"
        val nodeList = xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET) as NodeList

        return (0 until nodeList.length).map { i ->
            val item: Element = nodeList.item(i) as Element
            val groupId = xPath.evaluate("groupId", item) ?: ""
            val artifact = xPath.evaluate("artifactId", item) ?: ""

            DepDependency(
                name = "$groupId:$artifact",
                group = listOf(groupId),
                artifact = artifact,
                version = xPath.evaluate("version", item) ?: ""
            )
        }.toList()
    }
}