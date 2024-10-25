package org.archguard.linter.rule.sql

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.archguard.context.CodeDatabaseRelation
import org.junit.jupiter.api.Test

internal class DatamapRuleVisitorTest {
    @Test
    internal fun from_json() {
        var data = """
[
  {
    "packageName": "com.thoughtworks.archguard.aac.infrastructure",
    "className": "AasDslRepositoryImpl",
    "functionName": "getById",
    "tables": [
      "aac_dsl"
    ],
    "sqls": [
      "select id, content, description, title from aac_dsl where id = ''"
    ]
  },
  {
    "packageName": "com.thoughtworks.archguard.change.infrastructure",
    "className": "DiffChangeDao",
    "functionName": "findBySystemId",
    "tables": [
      "scm_diff_change"
    ],
    "sqls": [
      "select system_id as systemId, since_rev as sinceRev, until_rev as untilRev, package_name as packageName, class_name as className, relations  from scm_diff_change where system_id = ''"
    ]
  },
  {
    "packageName": "com.thoughtworks.archguard.change.infrastructure",
    "className": "GitChangeDao",
    "functionName": "findBySystemId",
    "tables": [
      "scm_git_hot_file"
    ],
    "sqls": [
      "select * from scm_git_hot_file where system_id = ''"
    ]
  }]"""

        val rels: List<CodeDatabaseRelation> = Json.decodeFromString(data)
        val visitor = DatamapRuleVisitor(rels)

        val ruleSetProvider = SqlRuleSetProvider()
        val results = visitor.visitor(listOf(ruleSetProvider.get()))

        kotlin.test.assertEquals(1, results.size)
    }
}