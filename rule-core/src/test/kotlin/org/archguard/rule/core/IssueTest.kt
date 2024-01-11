package org.archguard.rule.core;

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.rule.core.Issue
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.RuleType
import org.archguard.rule.core.Severity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class IssueTest {

    @Test
    fun `should return correct JSON string representation`() {
        // given
        val position = IssuePosition(1, 2, 3, 4, mapOf())
        val ruleId = "RULE001"
        val name = "Rule Name"
        val detail = "Rule Detail"
        val ruleType = RuleType.CUSTOM
        val severity = Severity.INFO

        val issue = Issue(position, ruleId, name, detail, ruleType, severity)

        // when
        val jsonString = issue.toString()

        // then
        val expectedJsonString = "{\"position\":{\"startLine\":1,\"startColumn\":2,\"endLine\":3,\"endColumn\":4},\"ruleId\":\"RULE001\",\"name\":\"Rule Name\",\"detail\":\"Rule Detail\",\"ruleType\":\"CUSTOM\",\"severity\":\"INFO\"}"
        assertEquals(expectedJsonString, jsonString)
    }
}