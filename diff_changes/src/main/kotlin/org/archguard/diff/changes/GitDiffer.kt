package org.archguard.diff.changes

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.dircache.DirCacheIterator
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.ObjectLoader
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.treewalk.FileTreeIterator
import org.eclipse.jgit.treewalk.TreeWalk
import java.io.File
import java.nio.charset.StandardCharsets

class DifferFile(
    val content: String,
    val hash: String,
    val path: String,
)

object GitDiffer {
    fun getRange(path: String, branch: String = "master", sinceRev: String, untilRev: String) {
        val repPath = File(path)
        val repository = FileRepositoryBuilder().findGitDir(repPath).build()
        val git = Git(repository).specifyBranch(branch)

        val since: ObjectId = git.repository.resolve(sinceRev)
        val until: ObjectId = git.repository.resolve(untilRev)

        createBaselineAst(repository, since)

        for (commit in git.log().addRange(since, until).call()) {
            // todo: add increment for path changes
        }
    }

    private fun createBaselineAst(repository: Repository, since: ObjectId) {
        val rw = RevWalk(repository)
        val tw = TreeWalk(repository)

        val commitToCheck: RevCommit = rw.parseCommit(since)
        tw.addTree(commitToCheck.tree)
        tw.addTree(DirCacheIterator(repository.readDirCache()))
        tw.addTree(FileTreeIterator(repository))

        tw.isRecursive = true;
        while (tw.next()) {

            try {
                val pathString = tw.pathString
                val blobId: ObjectId = tw.getObjectId(0)

                if (pathString.endsWith(".kt")) {
                    println(tw.pathString)

                    repository.newObjectReader().use { objectReader ->
                        val objectLoader: ObjectLoader = objectReader.open(blobId)
                        val bytes: ByteArray = objectLoader.bytes
                        val content = String(bytes, StandardCharsets.UTF_8)
                        content
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun Git.specifyBranch(branch: String): Git {
        checkout().setName(branch).call()
        return this
    }

}