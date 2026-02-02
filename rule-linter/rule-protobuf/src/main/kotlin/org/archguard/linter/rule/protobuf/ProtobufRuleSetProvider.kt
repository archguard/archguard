package org.archguard.linter.rule.protobuf

import org.archguard.linter.rule.protobuf.rules.FileNamesLowerSnakeCaseRule
import org.archguard.linter.rule.protobuf.rules.FieldNamesLowerSnakeCaseRule
import org.archguard.linter.rule.protobuf.rules.ImportsSortedRule
import org.archguard.linter.rule.protobuf.rules.MessageNamesUpperCamelCaseRule
import org.archguard.linter.rule.protobuf.rules.PackageNameLowerCaseRule
import org.archguard.linter.rule.protobuf.rules.RpcNamesUpperCamelCaseRule
import org.archguard.linter.rule.protobuf.rules.ServiceNamesUpperCamelCaseRule
import org.archguard.rule.core.RuleSet
import org.archguard.rule.core.RuleSetProvider
import org.archguard.rule.core.RuleType

class ProtobufRuleSetProvider : RuleSetProvider {
    override fun get(): RuleSet {
        return RuleSet(
            RuleType.PROTOBUF_SMELL,
            "normal",
            FileNamesLowerSnakeCaseRule(),
            PackageNameLowerCaseRule(),
            ImportsSortedRule(),
            MessageNamesUpperCamelCaseRule(),
            FieldNamesLowerSnakeCaseRule(),
            ServiceNamesUpperCamelCaseRule(),
            RpcNamesUpperCamelCaseRule(),
        )
    }
}