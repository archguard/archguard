package org.archguard.scanner.ctl.loader.rule

import org.archguard.scanner.core.sourcecode.ContainerResource
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class RuleLoaderTest {
    @Test
    internal fun load() {
        val jar = this.javaClass.classLoader.getResource("kotlin/testonly-rule-webapi-2.0.0-alpha.2.jar")!!

        val resource = ContainerResource(sourceUrl = "api/book/delete/{bookId}")
        val ruleName = "org.archguard.linter.rule.webapi.WebApiRuleVisitor"

        val (ruleVisitor, ruleSetProviders) = RuleLoader.load(listOf(resource), LinterSpec(jar.path, ruleName))

        val issues = ruleVisitor.visitor(ruleSetProviders.map { it.get() })

        assertEquals(1, ruleSetProviders.size)
        assertEquals(1, issues.size)
    }
}
