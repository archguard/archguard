package com.thoughtworks.archguard.git.scanner

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.diff.DiffFormatter
import org.eclipse.jgit.diff.RawTextComparator
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.treewalk.TreeWalk
import org.eclipse.jgit.util.io.DisabledOutputStream
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File
import java.nio.charset.StandardCharsets


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
    val logger: Logger = LoggerFactory.getLogger(JGitAdapter::class.java)

    override fun scan(config: Config, publish: (Any) -> Unit) {
        val repPath = File(config.path)
        logger.info("git repository locate at {}", repPath.absolutePath)
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
                                        commit_time = revCommit.commitTime,
                                        committer_name = revCommit.committerIdent.name,
                                        commit_email = revCommit.committerIdent.emailAddress,
                                        rep_id = repId)
                                publish(commit)

                                val parent: RevCommit? = if (revCommit.parentCount == 0) null else revCommit.getParent(0)
                                diffFormatter.scan(parent?.tree, revCommit.tree).forEach {
                                    val javaFile = it.newPath.endsWith(".java") && it.changeType != DiffEntry.ChangeType.DELETE
                                    if (javaFile) {
                                        TreeWalk.forPath(repository, it.newPath, revCommit.tree).use { treeWalk ->
                                            if (treeWalk != null) {
                                                val objectId = treeWalk.getObjectId(0)
                                                repository!!.newObjectReader().use { objectReader ->
                                                    val bytes = objectReader.open(objectId).bytes
                                                    println(String(bytes, StandardCharsets.UTF_8))
                                                }
                                            }
                                        }
                                    }
                                    val changeEntry = ChangeEntry(old_path = it.oldPath,
                                            new_path = it.newPath,
                                            mode = it.changeType.name,
                                            commit_id = revCommit.name)
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


