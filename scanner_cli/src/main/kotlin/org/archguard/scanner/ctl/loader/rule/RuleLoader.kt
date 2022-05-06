package org.archguard.scanner.ctl.loader

import org.archguard.rule.core.RuleSetProvider
import java.util.ServiceLoader

/**
 * **RuleLoader** is load ruleSets by classes
 */
class RuleLoader {
    fun load() {

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
