package org.archguard.scanner.ctl.rule

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.ctl.impl.OfficialAnalyserSpecs

class RuleSlotTest {

    @Test
    fun shouldReturnWebApiAnalyserSpec() {
        // given
        val name = "webapi"
        val expectedSpec = AnalyserSpec(
            "rule-webapi",
            OfficialAnalyserSpecs.host(),
            OfficialAnalyserSpecs.RULE.version(),
            "rule-webapi-${OfficialAnalyserSpecs.RULE.version()}-all.jar",
            className = "org.archguard.linter.rule.webapi.WebApiRuleSlot",
            "rule"
        )

        // when
        val actualSpec = RuleSlot.fromName(name)

        // then
        assertEquals(expectedSpec, actualSpec)
    }

    @Test
    fun shouldReturnTestAnalyserSpec() {
        // given
        val name = "test"
        val expectedSpec = AnalyserSpec(
            "rule-test",
            OfficialAnalyserSpecs.host(),
            OfficialAnalyserSpecs.RULE.version(),
            "rule-test-${OfficialAnalyserSpecs.RULE.version()}-all.jar",
            className = "org.archguard.linter.rule.testcode.TestSmellRuleSlot",
            "rule"
        )

        // when
        val actualSpec = RuleSlot.fromName(name)

        // then
        assertEquals(expectedSpec, actualSpec)
    }

    @Test
    fun shouldReturnSqlAnalyserSpec() {
        // given
        val name = "sql"
        val expectedSpec = AnalyserSpec(
            "rule-sql",
            OfficialAnalyserSpecs.host(),
            OfficialAnalyserSpecs.RULE.version(),
            "rule-sql-${OfficialAnalyserSpecs.RULE.version()}-all.jar",
            className = "org.archguard.linter.rule.sql.DatamapRuleSlot",
            "rule"
        )

        // when
        val actualSpec = RuleSlot.fromName(name)

        // then
        assertEquals(expectedSpec, actualSpec)
    }

    @Test
    fun shouldReturnNullForUnknownName() {
        // given
        val name = "unknown"

        // when
        val actualSpec = RuleSlot.fromName(name)

        // then
        assertEquals(null, actualSpec)
    }
}
