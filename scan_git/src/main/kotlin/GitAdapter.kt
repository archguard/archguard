package com.thoughtworks.archguard.git.scanner

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.DiffFormatter
import org.eclipse.jgit.diff.RawTextComparator
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.util.io.DisabledOutputStream
import org.springframework.stereotype.Component
import java.io.File


interface GitAdapter {
    fun scan(config: Config): CommitHistory
}

/**
 * @param lastCommit  last commit hash in database
 */
data class Config(val path: String, val branch: String? = null, val lastCommit: String? = null)


/** this git adaptor utilize  JGit API to access git repository*/
@Component
class JGitAdapter : GitAdapter {
    //    todo: 考虑内存溢出的问题
    override fun scan(config: Config): CommitHistory {
        FileRepositoryBuilder().path(config.path).use { repository ->
            Git(repository).specifyBranch(config.branch).use { git ->
                DiffFormatter(DisabledOutputStream.INSTANCE).config(repository).use { diffFormatter ->
                    return git.commitHistory(diffFormatter)
                }
            }
        }
    }

    private fun Git.commitHistory(diffFormatter: DiffFormatter): CommitHistory {
        val commits = log().call().map { revCommit ->
            val changes = getChangeList(revCommit, diffFormatter)
            val committer = Committer(revCommit.committerIdent.name, revCommit.committerIdent.emailAddress)
            Commit(revCommit.commitTime, revCommit.name, committer, changes)
        }
        return CommitHistory(repository.branch, commits)
    }

    /*parentCount==0 就是第一个Commit, parentCount>1 意味着 merge */
    private fun getChangeList(revCommit: RevCommit, diffFormatter: DiffFormatter): List<ChangeEntry> {
        val parent: RevCommit? = if (revCommit.parentCount == 0) null else revCommit.getParent(0)
        return diffFormatter.scan(parent?.tree, revCommit.tree).map {
            ChangeEntry(oldPath = it.oldPath, newPath = it.newPath, mode = it.changeType.name)
        }
    }


    private fun DiffFormatter.config(repository: Repository): DiffFormatter {
        setRepository(repository)
        setDiffComparator(RawTextComparator.DEFAULT)
        isDetectRenames = true
        return this
    }

    private fun FileRepositoryBuilder.path(path: String): Repository {
        return readEnvironment()
                .findGitDir(File(path))
                .build()
    }

    /*specify git branch*/
    private fun Git.specifyBranch(branch: String?): Git {
        checkout().setName(branch ?: "master").call()
        return this
    }
}