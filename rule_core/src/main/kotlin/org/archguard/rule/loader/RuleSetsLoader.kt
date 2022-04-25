package org.archguard.rule.loader

import org.archguard.rule.core.RuleSetProvider
import java.util.ServiceLoader

// load ruleSets by classes
class RuleSetsLoader {
    // todo: thinking others way.
    fun load(disabledRules: String) {
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