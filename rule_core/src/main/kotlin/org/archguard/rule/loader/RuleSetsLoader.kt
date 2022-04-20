package org.archguard.rule.loader

import org.archguard.rule.core.RuleSetProvider
import java.util.ServiceLoader

// load rulesets by classes
class RuleSetsLoader {
    // todo: thinking others way.
    fun load() {
        val loader: ServiceLoader<RuleSetProvider> = ServiceLoader.load(RuleSetProvider::class.java)
        for (service in loader) {
            println("Log written by $service")
        }
    }
}