package org.archguard.scanner.core.git

import org.archguard.scanner.core.Analyser
import org.archguard.scanner.core.client.dto.GitLogs

interface GitAnalyser : Analyser<GitContext> {
    fun analyse(): GitLogs
}
