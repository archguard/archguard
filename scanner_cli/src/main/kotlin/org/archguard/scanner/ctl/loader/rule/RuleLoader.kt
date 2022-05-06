package org.archguard.scanner.ctl.loader.rule

import org.archguard.rule.core.RuleSetProvider
import org.archguard.rule.core.RuleVisitor
import java.io.File
import java.net.URLClassLoader
import java.util.ServiceLoader


data class LinterSpec(
    val jar: String,
    var className: String,
)

/**
 * **RuleLoader** is load ruleSets by classes
 */
object RuleLoader {
    fun load(data: List<Any>, spec: LinterSpec): Pair<RuleVisitor, List<RuleSetProvider>> {
        val jarUrl = File(spec.jar).toURI().toURL()
        val loader = URLClassLoader(arrayOf(jarUrl))
        val ruleSetProviders = ServiceLoader.load(RuleSetProvider::class.java, loader).toList()
        val visitor = Class.forName(spec.className, true, loader)
            .declaredConstructors[0]
            .newInstance(data) as RuleVisitor

        return visitor to ruleSetProviders
    }

    fun getRuleSetProvidersByFeatSpecs() {

    }

    fun loadRules(disabledRules: String) {
        // 1. load classes from jar
        val loader: ServiceLoader<RuleSetProvider> = ServiceLoader.load(RuleSetProvider::class.java)
        for (service in loader) {
            println("Log written by $service")
        }

        // 2. split disabled_rules

        // 3. filter RuleSetProvider

        // 4. filter disableRules with VisitorProvider
    }
}
