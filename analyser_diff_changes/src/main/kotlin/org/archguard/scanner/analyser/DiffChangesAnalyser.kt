package org.archguard.scanner.analyser

import org.archguard.scanner.analyser.diffchanges.GitDiffer
import org.archguard.scanner.core.diffchanges.ChangedCall
import org.archguard.scanner.core.diffchanges.DiffChangesContext
import org.slf4j.LoggerFactory

class DiffChangesAnalyser(
    override val context: DiffChangesContext
) : org.archguard.scanner.core.diffchanges.DiffChangesAnalyser {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        const val SHORT_ID_LENGTH = 7
    }

    override fun analyse(): List<ChangedCall> = context.run {
        logger.info("diff from $since to $until on branch: $branch with path: $path")
        val differ = GitDiffer(path, branch, depth)
        val sinceRev = since.substring(0, minOf(SHORT_ID_LENGTH, since.length))
        val untilRev = until.substring(0, minOf(SHORT_ID_LENGTH, until.length))
        val changedCalls = differ.countBetween(sinceRev, untilRev)

        client.saveDiffs(changedCalls)

        changedCalls
    }
}
