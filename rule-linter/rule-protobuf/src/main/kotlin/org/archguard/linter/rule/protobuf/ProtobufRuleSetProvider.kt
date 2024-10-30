package org.archguard.linter.rule.protobuf

import org.archguard.rule.core.*

class ProtobufRuleSetProvider : RuleSetProvider {
    override fun get(): RuleSet {
        return RuleSet(
            RuleType.PROTOBUF_SMELL,
            "normal",
            SampleRule(),
        )
    }
}

class SampleRule : ProtobufRule() {
    override fun visitSegment(it: String, context: RuleContext, callback: IssueEmit) {

    }
}