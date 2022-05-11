package org.archguard.scanner.core.diffchanges

import org.archguard.scanner.core.Analyser
import org.archguard.scanner.core.client.dto.ChangedCall

interface DiffChangesAnalyser : Analyser<DiffChangesContext> {
    fun analyse(): List<ChangedCall>
}
