package org.archguard.scanner.ctl.loader.rule

import org.archguard.rule.core.Issue
import org.archguard.rule.core.RuleSetProvider
import org.archguard.rule.core.RuleVisitor
import org.archguard.scanner.ctl.command.ScannerCommand

/**
 * **RuleDispatcher** is to load rules
 */
class RuleDispatcher {
    fun dispatch(command: ScannerCommand) {

    }

    // todo: add disable rule checks
    fun executeOne(ruleVisitor: RuleVisitor, ruleSetProvider: RuleSetProvider): List<Issue> {
        return ruleVisitor.visitor(listOf(ruleSetProvider.get()))
    }
}
