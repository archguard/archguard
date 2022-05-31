package org.archguard.scanner.ctl.loader

import org.archguard.rule.core.Rule
import org.archguard.scanner.core.Analyser
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context


interface RuleCliContext : Context {
    override val type: AnalyserType get() = AnalyserType.RULE

    val language: String
}

interface RuleAnalyser: Analyser<RuleCliContext> {
    fun analyse(): List<Rule>
}
