package org.archguard.linter.rule.protobuf

import chapi.domain.core.CodeDataStruct
import org.archguard.rule.core.Issue
import org.archguard.rule.core.IssuePosition
import org.archguard.rule.core.Rule
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleType
import org.archguard.rule.core.RuleVisitor

class ProtobufRuleVisitor(private val structs: List<CodeDataStruct>) : RuleVisitor(structs) {
    override fun visitor(ruleSets: Iterable<RuleSet>): List<Issue> {
        val results: MutableList<Issue> = mutableListOf()
        val protoStructs = structs.filter { it.FilePath.endsWith(".proto") }
        if (protoStructs.isEmpty()) return results

        val ctx = ProtobufRuleContext(ProtobufLintConfigLoader.load())
        val fileToStructs = protoStructs.groupBy { it.FilePath }

        for ((filePath, structsInFile) in fileToStructs) {
            for (ruleSet in ruleSets) {
                for (rule in ruleSet.rules) {
                    val protobufRule = rule as? ProtobufRule ?: continue
                    val ruleId = protobufRule.id.ifBlank { protobufRule.key }

                    if (!ctx.config.isRuleEnabled(ruleId)) continue
                    if (ctx.config.isIgnored(ruleId, filePath)) continue

                    protobufRule.visitFile(filePath, structsInFile, ctx, fun(emittedRule: Rule, position: IssuePosition) {
                        results += Issue(
                            position = position,
                            ruleId = emittedRule.key,
                            name = emittedRule.name,
                            detail = emittedRule.description,
                            ruleType = RuleType.PROTOBUF_SMELL,
                            severity = emittedRule.severity,
                            fullName = buildFullName(structsInFile),
                            source = filePath,
                        )
                    })
                }
            }
        }

        return results
    }

    private fun buildFullName(structsInFile: List<CodeDataStruct>): String {
        val first = structsInFile.firstOrNull() ?: return ""
        return "${first.Module}:${first.Package}:${first.NodeName}"
    }
}