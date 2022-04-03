package org.archguard.diff.changes

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.diff.DiffFormatter
import org.eclipse.jgit.diff.RawTextComparator
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.util.io.DisabledOutputStream
import java.io.File

object GitDiffer {
    fun getRange(path: String, branch: String = "master", sinceRev: String, untilRev: String) {
        val repPath = File(path)
        val repository = FileRepositoryBuilder().findGitDir(repPath).build()
        val git = Git(repository).specifyBranch(branch)

        val since: ObjectId = git.repository.resolve(sinceRev)
        val until: ObjectId = git.repository.resolve(untilRev)
        for (commit in git.log().addRange(since, until).call()) {
            toChangeEntry(repository, commit)
        }
    }

    private fun toChangeEntry(repository: Repository, revCommit: RevCommit) {
        val diffFormatter = DiffFormatter(DisabledOutputStream.INSTANCE).config(repository)
        diffFormatter.scan(getParent(revCommit)?.tree, revCommit.tree)
            .map { d -> doConvertToChangeEntry(d, repository, revCommit, diffFormatter) }
    }

    private fun doConvertToChangeEntry(
        diffEntry: DiffEntry,
        repository: Repository,
        revCommit: RevCommit,
        df: DiffFormatter
    ) {
        try {
            println(diffEntry.oldPath)
            println(diffEntry.newPath)
            println(diffEntry.changeType.name)
        } catch (ex: Exception) {
            throw ex
        }

    }

    private fun getParent(revCommit: RevCommit): RevCommit? {
        return if (revCommit.parentCount == 0) {
            null
        } else
            revCommit.getParent(0)
    }

    private fun DiffFormatter.config(repository: Repository): DiffFormatter {
        setRepository(repository)
        setDiffComparator(RawTextComparator.DEFAULT)
        isDetectRenames = true
        return this
    }

    private fun Git.specifyBranch(branch: String): Git {
        checkout().setName(branch).call()
        return this
    }

}