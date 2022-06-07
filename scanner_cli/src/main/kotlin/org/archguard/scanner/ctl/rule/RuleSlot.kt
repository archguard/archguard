package org.archguard.scanner.ctl.rule

import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.ctl.impl.OfficialAnalyserSpecs

object RuleSlot {
    fun fromName(name: String): AnalyserSpec? {
        val url = OfficialAnalyserSpecs.host()
        val version = OfficialAnalyserSpecs.Rule.version()

        return when(name) {
            "webapi" ->  {
                AnalyserSpec(
                    "rule-webapi",
                    url,
                    version,
                    "rule-${name}-${OfficialAnalyserSpecs.Rule.version()}-all.jar",
                    className = "org.archguard.linter.rule.webapi.WebApiRuleSlot",
                    "rule"
                )
            }
            "test" ->  {
                AnalyserSpec(
                    "rule-test",
                    url,
                    version,
                    "rule-${name}-${OfficialAnalyserSpecs.Rule.version()}-all.jar",
                    className = "org.archguard.linter.rule.testcode.TestSmellRuleSlot",
                    "rule"
                )
            }
            "sql" ->  {
                AnalyserSpec(
                    "rule-sql",
                    url,
                    version,
                    "rule-${name}-${OfficialAnalyserSpecs.Rule.version()}-all.jar",
                    className = "org.archguard.linter.rule.sql.SqlRuleSlot",
                    "rule"
                )
            }
            else -> {
                null
            }
        }
    }
}