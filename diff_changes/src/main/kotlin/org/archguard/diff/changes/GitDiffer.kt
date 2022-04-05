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
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.diff.DiffFormatter
import org.eclipse.jgit.diff.RawTextComparator
import org.eclipse.jgit.util.io.DisabledOutputStream

class DifferFile(
    val path: String,
    val dataStructs: Array<CodeDataStruct>,
)

class ChangedEntry (
    val path: String,
    val className: String,
    val functionName: String
)


class GitDiffer(val path: String, val branch: String, val systemId: String, val language: String) {
    private var baseLineDataTree: List<DifferFile> = listOf()
    private val differFileMap: MutableMap<String, DifferFile> = mutableMapOf()
    private val changedDataStructs: MutableList<CodeDataStruct> = mutableListOf()

    fun countInRange(sinceRev: String, untilRev: String): MutableList<CodeDataStruct> {
        val repository = FileRepositoryBuilder().findGitDir(File(path)).build()
        val git = Git(repository).specifyBranch(branch)

        val since: ObjectId = git.repository.resolve(sinceRev)
        val until: ObjectId = git.repository.resolve(untilRev)

        // 1. create based ast model from since revision commit
        this.baseLineDataTree = createBaselineAstTree(repository, since)

        // 2. calculate changed files to utils file
        for (commit in git.log().addRange(since, until).call()) {
            getChangedFiles(repository, commit)
        }

        // 3. count changed items reverse-call functions


        // 4. align to the latest file path (maybe), like: increment for path changes
        return changedDataStructs
    }

    private fun getChangedFiles(repository: Repository, revCommit: RevCommit) {
        val diffFormatter = DiffFormatter(DisabledOutputStream.INSTANCE).config(repository)
        diffFormatter.scan(getParent(revCommit)?.tree, revCommit.tree)
            .map { d -> patchToDataStructs(d, repository, revCommit, diffFormatter) }
    }

    private fun patchToDataStructs(
        diffEntry: DiffEntry,
        repository: Repository,
        revCommit: RevCommit,
        df: DiffFormatter
    ) {
        try {
            val treeWalk = TreeWalk.forPath(repository, diffEntry.newPath, revCommit.tree)
            val pathString = treeWalk.pathString

            val blobId = treeWalk.getObjectId(0)

            val newDataStructs = diffFileFromBlob(repository, blobId, pathString)
            val oldDataStructs = this.differFileMap[pathString]!!.dataStructs

            // compare for sized
            if (newDataStructs.size != oldDataStructs.size) {
                // todo: make changed node to ds and return
                println("sized not equal")
            }

            // compare for field
            newDataStructs.forEachIndexed { index, ds ->
                // todo: check for fields change
                if (!ds.Fields.contentEquals(oldDataStructs[index].Fields)) {
                    println("fields not equal")
                }

                if (ds.Functions.size != oldDataStructs[index].Functions.size) {
                    // todo: find different functions
                    println("function size not equal")
                }

                // compare for function sizes
                if (!ds.Functions.contentEquals(oldDataStructs[index].Functions)) {
                    val difference = ds.Functions.filterNot { oldDataStructs[index].Functions.contains(it) }
                    difference.forEach {
                        println("changed functions:${it.Name}")
                    }
                }
            }

            this.changedDataStructs += newDataStructs
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

    private fun getParent(revCommit: RevCommit): RevCommit? {
        return if (revCommit.parentCount == 0) {
            null
        } else {
            revCommit.getParent(0)
        }
    }

    private fun createBaselineAstTree(repository: Repository, since: ObjectId): List<DifferFile> {
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
                    val dataStructs = diffFileFromBlob(repository, blobId, pathString)
                    val differFile = DifferFile(path = pathString, dataStructs = dataStructs)

                    differFileMap[pathString] = differFile
                    files += differFile
                }
            } catch (e: Exception) {
                println(e)
            }
        }

        return files
    }

    private fun diffFileFromBlob(
        repository: Repository,
        blobId: ObjectId,
        pathString: String
    ): Array<CodeDataStruct> {
        val content = repository.newObjectReader().use { objectReader ->
            val objectLoader: ObjectLoader = objectReader.open(blobId)
            val bytes: ByteArray = objectLoader.bytes
            val content = String(bytes, StandardCharsets.UTF_8)
            content
        }

        val file = AbstractFile(File(pathString).name, pathString, true, pathString, content)
        return KotlinAnalyserApp().analysisByFiles(arrayOf(file))
    }

    private fun Git.specifyBranch(branch: String): Git {
        checkout().setName(branch).call()
        return this
    }


}
