package com.thoughtworks.archguard.git.scanner

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.DiffFormatter
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.util.io.DisabledOutputStream
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
data class Config(val gitPath: String, var lastCommit: String)

class JGitAdapter : GitAdapter {
    override fun scan(config: Config): GitRepository {
        val path = config.gitPath
        val repository = buildRepository(path)
        val commits = Git(repository).log().all().call()
        val diffFormatter = DiffFormatter(DisabledOutputStream.INSTANCE)
        val commitList = commits.map {
            val parents = it.parents
            val diffs = diffFormatter.scan(it.tree, parents[0].tree)
            val changes = diffs.map { ChangeEntry(it.newPath, it.changeType.name) }
            val committer = Committer(it.committerIdent.name, it.committerIdent.emailAddress)
            Commit(it.commitTime, it.name, committer, changes)
        }

        return GitRepository(repository.branch, commitList)
    }

    private fun buildRepository(path: String?): Repository {
        val builder = FileRepositoryBuilder()
        return builder
                .readEnvironment()
                .findGitDir(File(path ?: "/Users/ygdong/git/jenkins-report"))
                .build()
    }
}