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
    fun scan(config: Config): GitRepository
}

/**
 *
 * @param lastCommit  last commit hash in database
 *
 *
 * */
data class Config(val path: String, val branch: String? = null, val lastCommit: String? = null)

fun DiffFormatter.config(repository: Repository): DiffFormatter {
    setRepository(repository)
    setDiffComparator(RawTextComparator.DEFAULT)
    isDetectRenames = true
    return this
}

@Component
class JGitAdapter : GitAdapter {
    override fun scan(config: Config): GitRepository {
        buildRepository(config).use { repository ->
            Git(repository).use { git ->
                git.checkout().setName(config.branch ?: "master").call() //todo : 是否可以不checkout branch ,避免本地库的修改
                DiffFormatter(DisabledOutputStream.INSTANCE).config(repository).use { diffFormatter ->
                    val commitList = getCommitList(git, diffFormatter)
                    return GitRepository(repository.branch, commitList)
                }
            }
        }
    }

    private fun getCommitList(git: Git, diffFormatter: DiffFormatter): List<Commit> {
        return git.log().call().map { revCommit ->
            val changes = getChangeList(revCommit, diffFormatter)
            val committer = Committer(revCommit.committerIdent.name, revCommit.committerIdent.emailAddress)
            Commit(revCommit.commitTime, revCommit.name, committer, changes)
        }
    }

    /*parentCount==0 就是第一个Commit, parentCount>1 意味着 merge */
    private fun getChangeList(revCommit: RevCommit, diffFormatter: DiffFormatter): List<ChangeEntry> {
        val parent: RevCommit? = if (revCommit.parentCount == 0) null else revCommit.getParent(0)
        return diffFormatter.scan(parent?.tree, revCommit.tree).map {
            ChangeEntry(oldPath = it.oldPath, newPath = it.newPath, mode = it.changeType.name)
        }
    }

    private fun buildRepository(config: Config): Repository {
        return FileRepositoryBuilder()
                .readEnvironment()
                .findGitDir(File(config.path))
                .build()
    }
}