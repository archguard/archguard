package org.archguard.scanner.core.git

import org.archguard.model.GitLogs
import org.archguard.scanner.core.Analyser

interface GitAnalyser : Analyser<GitContext> {
    fun analyse(): List<GitLogs>
}
