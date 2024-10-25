package org.archguard.scanner.analyser

import org.archguard.model.ChangeEntry
import org.archguard.model.CommitLog
import org.archguard.model.GitBranch
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ListBranchCommand
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.diff.DiffFormatter
import org.eclipse.jgit.diff.RawTextComparator
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.util.io.DisabledOutputStream
import java.io.File

data class GitResult(
    var logs: List<CommitLog>,
    var entries: List<ChangeEntry>,
    var branches: List<GitBranch>
)

class JGitLogAdapter {
    fun scan(path: String, branch: String = "master", startedAt: Long = 0, repoId: String, ): GitResult {
        val repPath = File(path)

        val repository = FileRepositoryBuilder().findGitDir(repPath).build()
        val gitRepo = Git(repository)
        val git = gitRepo.specifyBranch(branch)
        val revCommitSequence =
            git.log().call().asSequence().takeWhile { it.commitTime * 1000L > startedAt }.toList()

        val originBranches = gitRepo.branchList().setListMode(ListBranchCommand.ListMode.ALL).call()
        val gitBranches = originBranches.map { GitBranch(it.name) }.toList()

        val commitLog = revCommitSequence.map { toCommitLog(it, repoId) }.toList()
        val changeEntry = revCommitSequence.map { toChangeEntry(repository, it) }.flatten().toList()
        return GitResult(commitLog, changeEntry, gitBranches)
    }

    private fun toCommitLog(revCommit: RevCommit, repoId: String): CommitLog {
        val committer = revCommit.committerIdent
        val msg = revCommit.shortMessage
        return CommitLog(
            id = revCommit.name,
            commitTime = committer.`when`.time,
            shortMessage = if (msg.length < 200) msg else msg.substring(0, 200),
            committerName = committer.name,
            committerEmail = committer.emailAddress,
            repositoryId = repoId,
        )
    }

    private fun toChangeEntry(repository: Repository, revCommit: RevCommit): List<ChangeEntry> {
        val diffFormatter = DiffFormatter(DisabledOutputStream.INSTANCE).config(repository)
        return diffFormatter.scan(getParent(revCommit)?.tree, revCommit.tree)
            .map { d -> doConvertToChangeEntry(d, revCommit, diffFormatter) }
    }

    private fun getParent(revCommit: RevCommit): RevCommit? {
        return if (revCommit.parentCount == 0) {
            null
        } else
            revCommit.getParent(0)
    }

    private fun doConvertToChangeEntry(
        diffEntry: DiffEntry,
        revCommit: RevCommit,
        df: DiffFormatter
    ): ChangeEntry {
        try {
            var linesDeleted = 0
            var linesAdded = 0
            for (edit in df.toFileHeader(diffEntry).toEditList()) {
                linesDeleted += edit.endA - edit.beginA
                linesAdded += edit.endB - edit.beginB
            }

            return ChangeEntry(
                oldPath = diffEntry.oldPath,
                newPath = diffEntry.newPath,
                commitTime = revCommit.committerIdent.`when`.time,
                cognitiveComplexity = 0,
                changeMode = diffEntry.changeType.name,
                commitId = revCommit.name,
                // for knowledge map
                committer = revCommit.authorIdent.name,
                lineAdded = linesAdded,
                lineDeleted = linesDeleted,
            )
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun DiffFormatter.config(repository: Repository): DiffFormatter {
        setRepository(repository)
        setDiffComparator(RawTextComparator.DEFAULT)
        isDetectRenames = true
        return this
    }

    /* specify git branch */
    private fun Git.specifyBranch(branch: String): Git {
        checkout().setName(branch).call()
        return this
    }

    fun countChangesByPath(changeEntries: List<ChangeEntry>): HashMap<String, Int> {
        val changeCounts: HashMap<String, Int> = hashMapOf()
        changeEntries.reversed().forEach {
            when (it.changeMode) {
                "ADD" -> {
                    changeCounts[it.newPath] = 1
                }
                "RENAME" -> {
                    if (changeCounts[it.oldPath] == null) {
                        changeCounts[it.oldPath] = 1
                    }

                    changeCounts[it.newPath] = changeCounts[it.oldPath]!!
                    changeCounts.remove(it.oldPath)
                }
                "DELETE" -> {
                    changeCounts.remove(it.oldPath)
                }
                else -> {
                    if (changeCounts[it.newPath] == null) {
                        changeCounts[it.newPath] = 1
                    }
                    changeCounts[it.newPath] = changeCounts[it.newPath]!! + 1
                }
            }
        }

        return changeCounts
    }
}


