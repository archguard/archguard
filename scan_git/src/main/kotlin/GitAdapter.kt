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


/**
 * @param path  repository location
 * @param branch  branch name, default is master
 */
data class Config(val path: String, val branch: String = "master")


interface GitAdapter {
    fun scan(config: Config, publish: (Any) -> Unit)
}

@Component
class JGitAdapter : GitAdapter {
    override fun scan(config: Config, publish: (Any) -> Unit) {
        val repPath = File(config.path)
        val repId = System.nanoTime()
        publish(GitRepository(repPath.absolutePath, config.branch, id = repId))
        FileRepositoryBuilder()
                .findGitDir(repPath)
                .build()
                .use { repository ->
                    Git(repository).specifyBranch(config.branch).use { git ->
                        DiffFormatter(DisabledOutputStream.INSTANCE).config(repository).use { diffFormatter ->
                            git.log().call().forEach { revCommit ->
                                val commit = Commit(id = revCommit.name,
                                        time = revCommit.commitTime,
                                        committerName = revCommit.committerIdent.name,
                                        committerEmail = revCommit.committerIdent.emailAddress,
                                        repositoryId = repId)
                                publish(commit)

                                val parent: RevCommit? = if (revCommit.parentCount == 0) null else revCommit.getParent(0)
                                diffFormatter.scan(parent?.tree, revCommit.tree).forEach {
                                    val changeEntry = ChangeEntry(oldPath = it.oldPath,
                                            newPath = it.newPath,
                                            mode = it.changeType.name,
                                            commit = revCommit.name)
                                    publish(changeEntry)
                                }
                            }
                        }
                    }
                }
    }


    private fun DiffFormatter.config(repository: Repository): DiffFormatter {
        setRepository(repository)
        setDiffComparator(RawTextComparator.DEFAULT)
        isDetectRenames = true
        return this
    }

    /*specify git branch*/
    private fun Git.specifyBranch(branch: String): Git {
        checkout().setName(branch).call()
        return this
    }
}


