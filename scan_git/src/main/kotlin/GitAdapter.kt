package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.complexity.CognitiveComplexityParser
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
import org.springframework.beans.factory.annotation.Autowired
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
class JGitAdapter(@Autowired val cognitiveComplexityParser: CognitiveComplexityParser) : GitAdapter {
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
                                val committer = revCommit.committerIdent
                                val msg = revCommit.shortMessage

                                val commit = CommitLog(id = revCommit.name,
                                        commitTime = committer.`when`.time,
                                        shortMessage = if (msg.length < 200) msg else msg.substring(0, 200),
                                        committerName = committer.name,
                                        committerEmail = committer.emailAddress,
                                        repositoryId = repId)
                                publish(commit)

                                val parent: RevCommit? = if (revCommit.parentCount == 0) null else revCommit.getParent(0)
                                diffFormatter.scan(parent?.tree, revCommit.tree).forEach {
                                    val classComplexity: Int = cognitiveComplexityForJavaFile(it, repository, revCommit)
                                    val changeEntry = ChangeEntry(oldPath = it.oldPath,
                                            newPath = it.newPath,
                                            cognitiveComplexity = classComplexity,
                                            changeMode = it.changeType.name,
                                            commitId = revCommit.name)
                                    publish(changeEntry)
                                }
                            }
                        }
                    }
                }
    }

    private fun cognitiveComplexityForJavaFile(it: DiffEntry, repository: Repository, revCommit: RevCommit): Int {
        var classComplexity = 0
        val javaFile = it.newPath.endsWith(".java")
        if (javaFile) {
            TreeWalk.forPath(repository, it.newPath, revCommit.tree).use { treeWalk ->
                if (treeWalk != null) {
                    val objectId = treeWalk.getObjectId(0)
                    repository.newObjectReader().use { objectReader ->
                        val bytes = objectReader.open(objectId).bytes
                        val code = String(bytes, StandardCharsets.UTF_8)
                        val cplx = cognitiveComplexityParser.processCode(code)
                        classComplexity = cplx.sumBy { it.complexity }
                    }
                }
            }
        }
        return classComplexity
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


