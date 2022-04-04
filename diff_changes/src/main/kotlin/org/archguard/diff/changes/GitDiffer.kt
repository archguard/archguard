package org.archguard.diff.changes

import chapi.app.analyser.KotlinAnalyserApp
import chapi.app.analyser.support.AbstractFile
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
import chapi.domain.core.CodeDataStruct

class DifferFile(
    val path: String,
    val dataStructs: Array<CodeDataStruct>
)

class GitDiffer(val path: String, val branch: String, val systemId: String, val language: String) {
    fun countInRange(sinceRev: String, untilRev: String) {
        val repository = FileRepositoryBuilder().findGitDir(File(path)).build()
        val git = Git(repository).specifyBranch(branch)

        val since: ObjectId = git.repository.resolve(sinceRev)
        val until: ObjectId = git.repository.resolve(untilRev)

        val baseLineDataTree = createBaselineAst(repository, since)

        for (commit in git.log().addRange(since, until).call()) {
            // todo: add increment for path changes
        }
    }

    fun createBaselineAst(repository: Repository, since: ObjectId): List<DifferFile> {
        val rw = RevWalk(repository)
        val tw = TreeWalk(repository)

        val commitToCheck: RevCommit = rw.parseCommit(since)
        tw.addTree(commitToCheck.tree)
        tw.addTree(DirCacheIterator(repository.readDirCache()))
        tw.addTree(FileTreeIterator(repository))

        tw.isRecursive = true;

        val files: MutableList<DifferFile> = mutableListOf()
        while (tw.next()) {
            try {
                val pathString = tw.pathString
                val blobId: ObjectId = tw.getObjectId(0)

                if (pathString.endsWith(".kt")) {
                    val content = repository.newObjectReader().use { objectReader ->
                        val objectLoader: ObjectLoader = objectReader.open(blobId)
                        val bytes: ByteArray = objectLoader.bytes
                        val content = String(bytes, StandardCharsets.UTF_8)
                        content
                    }

                    val file = AbstractFile(File(pathString).name, pathString, true, pathString, content)
                    val dataStructs = KotlinAnalyserApp().analysisByFiles(arrayOf(file))

                    files += DifferFile(path = pathString, dataStructs = dataStructs)
                }
            } catch (e: Exception) {

            }
        }

        return files
    }

    private fun Git.specifyBranch(branch: String): Git {
        checkout().setName(branch).call()
        return this
    }


}
