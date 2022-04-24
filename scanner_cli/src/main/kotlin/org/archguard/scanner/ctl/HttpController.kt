package org.archguard.scanner.ctl

import org.archguard.scanner.ctl.command.ScannerCommand
import org.archguard.scanner.ctl.loader.AnalyserDispatcher

// TODO set up a http server to accept scanner command
interface HttpController {
    fun execute(command: ScannerCommand) {
        AnalyserDispatcher().dispatch(command)
    }
}
