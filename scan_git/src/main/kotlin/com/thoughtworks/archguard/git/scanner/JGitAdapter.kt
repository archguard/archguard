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
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.charset.StandardCharsets

/**
 * @param path  repository location
 * @param branch  branch name, default is master
 */

class JGitAdapter(private val cognitiveComplexityParser: CognitiveComplexityParser, val language: String) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun scan(path: String, branch: String = "master", after: String = "0", repoId: String, systemId: Long):
            Pair<List<CommitLog>, List<ChangeEntry>> {

        val repPath = File(path)

        val repository = FileRepositoryBuilder().findGitDir(repPath).build()
        val git = Git(repository).specifyBranch(branch)
        val revCommitSequence =
            git.log().call().asSequence().takeWhile { it.commitTime * 1000L > after.toLong() }.toList()

        val commitLog = revCommitSequence.map { toCommitLog(it, repoId, systemId) }.toList()
        val changeEntry = revCommitSequence.map { toChangeEntry(repository, it, systemId) }.flatten().toList()
        return Pair(commitLog, changeEntry)
    }

    private fun toCommitLog(revCommit: RevCommit, repoId: String, systemId: Long): CommitLog {
        val committer = revCommit.committerIdent
        val msg = revCommit.shortMessage
        return CommitLog(
            id = revCommit.name,
            commitTime = committer.`when`.time,
            shortMessage = if (msg.length < 200) msg else msg.substring(0, 200),
            committerName = committer.name,
            committerEmail = committer.emailAddress,
            repositoryId = repoId,
            systemId = systemId
        )
    }

    private fun toChangeEntry(repository: Repository, revCommit: RevCommit, systemId: Long): List<ChangeEntry> {
        val diffFormatter = DiffFormatter(DisabledOutputStream.INSTANCE).config(repository)
        return diffFormatter.scan(getParent(revCommit)?.tree, revCommit.tree)
            .map { d -> doConvertToChangeEntry(d, repository, revCommit, systemId) }
    }

    private fun getParent(revCommit: RevCommit): RevCommit? {
        return if (revCommit.parentCount == 0) {
            null
        } else
            revCommit.getParent(0)
    }

    private fun doConvertToChangeEntry(
        diffEntry: DiffEntry,
        repository: Repository,
        revCommit: RevCommit,
        systemId: Long
    ): ChangeEntry {
        try {
            var classComplexity: Int = 0
            if(this.language == "java") {
                classComplexity = cognitiveComplexityForJavaFile(diffEntry, repository, revCommit)
            }
            return ChangeEntry(
                oldPath = diffEntry.oldPath,
                newPath = diffEntry.newPath,
                commitTime = revCommit.committerIdent.`when`.time,
                cognitiveComplexity = classComplexity,
                changeMode = diffEntry.changeType.name,
                systemId = systemId,
                commitId = revCommit.name,
                // for knowledge map
                committer = revCommit.authorIdent.name
            )
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun cognitiveComplexityForJavaFile(
        diffEntry: DiffEntry,
        repository: Repository,
        revCommit: RevCommit
    ): Int {
        val isFileRemoved = diffEntry.newPath == "/dev/null" || diffEntry.oldPath == "/dev/null"
        if (isFileRemoved) {
            return 0
        }

        try {
            val treeWalk = TreeWalk.forPath(repository, diffEntry.newPath, revCommit.tree) ?: return 0
            val objectId = treeWalk.getObjectId(0)
            val code = String(repository.newObjectReader().open(objectId).bytes, StandardCharsets.UTF_8)
            val cplx = cognitiveComplexityParser.processCode(code)
            return cplx.sumOf { it.complexity }
        } catch (ex: Exception) {
            logger.error(
                "Fail to read file from DiffEntry {} / {}, {}, RevCommit from {} @ {} throw exception {}",
                diffEntry.oldPath, diffEntry.newPath, diffEntry.changeType.name,
                revCommit.authorIdent.name, revCommit.authorIdent.getWhen(), ex
            )
        }

        return 0
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


