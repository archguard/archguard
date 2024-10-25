package org.archguard.scanner.core.diffchanges

import org.archguard.model.ChangedCall
import org.archguard.scanner.core.Analyser

interface DiffChangesAnalyser : Analyser<DiffChangesContext> {
    fun analyse(): List<ChangedCall>
}
