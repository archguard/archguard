package org.archguard.scanner.ctl.loader.rule

import org.archguard.scanner.ctl.command.ScannerCommand

/**
 * **RuleDispatcher** is to load rules
 */
class RuleDispatcher {
    fun dispatch(command: ScannerCommand) {
        command.getAnalyserSpecs().forEach {

        }
    }
}
