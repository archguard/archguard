package org.archguard.scanner.analyser.diffchanges

import chapi.ast.kotlinast.AnalysisMode
import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.Serializable
import org.archguard.scanner.core.client.dto.ChangeRelation
import org.archguard.scanner.core.client.dto.ChangedCall
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.diff.DiffFormatter
import org.eclipse.jgit.diff.RawTextComparator
import org.eclipse.jgit.dircache.DirCacheIterator
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.treewalk.FileTreeIterator
import org.eclipse.jgit.treewalk.TreeWalk
import org.eclipse.jgit.util.io.DisabledOutputStream
import java.io.File
import java.nio.charset.StandardCharsets

@Serializable
class DifferFile(
    val path: String,
    val dataStructs: Array<CodeDataStruct>,
)

class ChangedEntry(
    val path: String,
    val file: String,
    val packageName: String,
    val className: String,
    val functionName: String = ""
)

class ChangedList(
    // if it had fields changed, make it as class changed.
    val files: List<ChangedEntry> = arrayListOf(),
    // if it had multiple class/structs changed, mark it as file level
    val classes: List<ChangedEntry> = arrayListOf(),
    // if it had parameter changed, function call changed, make it as Function level
    val functions: List<ChangedEntry> = arrayListOf(),
)

class GitDiffer(val path: String, val branch: String, val loopDepth: Int) {
    private var baseLineDataTree: List<DifferFile> = listOf()
    private val differFileMap: MutableMap<String, DifferFile> = mutableMapOf()
    private val changedFiles: MutableMap<String, ChangedEntry> = mutableMapOf()
    private val changedClasses: MutableMap<String, ChangedEntry> = mutableMapOf()
    private val changedFunctions: MutableMap<String, ChangedEntry> = mutableMapOf()

    fun countBetween(sinceRev: String, untilRev: String): List<ChangedCall> {
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

        // 3. count changed items reverse-call function
        this.genFunctionMap()
        this.genFunctionCallMap()
        val changedCalls = this.calculateChange()

        // add path map to projects

        // 4. align to the latest file path (maybe), like: increment for path changes
        return changedCalls
    }

    fun calculateChange(): List<ChangedCall> {
        return changedFunctions.map {
            val callName = it.value.packageName + "." + it.value.className + "." + it.value.functionName
            val changeRelations: MutableList<ChangeRelation> = mutableListOf()
            calculateReverseCalls(callName, changeRelations, loopDepth) ?: listOf()

            ChangedCall(
                path = it.value.path,
                packageName = it.value.packageName,
                className = it.value.className,
                relations = changeRelations
            )
        }.toList()
    }

    private var loopCount: Int = 0
    private var lastReverseCallChild: String = ""
    private fun calculateReverseCalls(
        funName: String,
        changeRelations: MutableList<ChangeRelation>,
        loopDepth: Int
    ): List<ChangeRelation>? {
        if (loopCount > loopDepth) {
            return null
        }

        loopCount++

        val calls = reverseCallMap[funName]
        calls?.forEach { child ->
            if (child == lastReverseCallChild) {
                return null
            }

            if (reverseCallMap[child] != null) {
                lastReverseCallChild = child
                val optRelations = calculateReverseCalls(child, changeRelations, loopDepth)
                if (optRelations != null) {
                    changeRelations += optRelations
                }
            }

            if (child != funName) {
                changeRelations += ChangeRelation(child, funName)
            }
        }

        return null
    }

    private val functionMap: MutableMap<String, Boolean> = mutableMapOf()
    fun genFunctionMap() {
        baseLineDataTree.forEach { file ->
            file.dataStructs.forEach { node ->
                node.Functions.forEach {
                    functionMap[node.Package + "." + node.NodeName + "." + it.Name] = true
                }
            }
        }
    }

    private val reverseCallMap: MutableMap<String, MutableList<String>> = mutableMapOf()
    fun genFunctionCallMap() {
        baseLineDataTree.forEach { file ->
            file.dataStructs.forEach { node ->
                node.Fields.forEach {
                    it.Calls.forEach {
                        // todo: add support for field call
                    }
                }

                node.Functions.forEach {
                    val caller = node.Package + "." + node.NodeName + "." + it.Name
                    it.FunctionCalls.forEach { codeCall ->
                        val callee = codeCall.buildFullMethodName()
                        if (functionMap[callee] != null) {
                            if (reverseCallMap[callee] == null) {
                                reverseCallMap[callee] = mutableListOf()
                            }

                            reverseCallMap[callee]!! += caller
                        }
                    }
                }
            }
        }
    }

    private fun getChangedFiles(repository: Repository, revCommit: RevCommit) {
        val diffFormatter = DiffFormatter(DisabledOutputStream.INSTANCE).config(repository)
        diffFormatter.scan(getParent(revCommit)?.tree, revCommit.tree)
            .map { d -> compareWithBaseline(d, repository, revCommit) }
    }

    private fun compareWithBaseline(
        diffEntry: DiffEntry,
        repository: Repository,
        revCommit: RevCommit
    ) {
        try {
            val treeWalk = TreeWalk.forPath(repository, diffEntry.newPath, revCommit.tree) ?: return

            val filePath = treeWalk.pathString

            val blobId = treeWalk.getObjectId(0)

            var newDataStructs: Array<CodeDataStruct> = arrayOf()

            val extension = File(filePath).extension
            newDataStructs = diffFileFromBlob(repository, blobId, filePath, extension)

            if (this.differFileMap[filePath] != null) {
                val oldDataStructs = this.differFileMap[filePath]!!.dataStructs

                // compare for sized
                if (newDataStructs.size != oldDataStructs.size) {
                    val difference = newDataStructs.filterNot { oldDataStructs.contains(it) }
                    difference.forEach {
                        this.changedFiles[filePath] = ChangedEntry(filePath, filePath, it.Package, it.NodeName)
                    }
                } else {
                    // compare for field
                    newDataStructs.forEachIndexed { index, ds ->
                        // in first version, if field changed, just make data structure change will be simple
                        if (ds.Fields.size != oldDataStructs[index].Fields.size) {
                            this.changedClasses[filePath] = ChangedEntry(filePath, filePath, ds.Package, ds.NodeName)
                        } else if (!ds.Fields.contentEquals(oldDataStructs[index].Fields)) {
                            this.changedClasses[filePath] = ChangedEntry(filePath, filePath, ds.Package, ds.NodeName)
                        }

                        // compare for function sizes
                        if (!ds.Functions.contentEquals(oldDataStructs[index].Functions)) {
                            val difference = ds.Functions.filterNot { oldDataStructs[index].Functions.contains(it) }
                            difference.forEach {
                                this.changedFunctions[filePath] =
                                    ChangedEntry(filePath, filePath, ds.Package, ds.NodeName, it.Name)
                            }
                        }
                    }
                }
            }
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

                val extension = File(pathString).extension

                val dataStructs = diffFileFromBlob(repository, blobId, pathString, extension)
                val differFile = DifferFile(path = pathString, dataStructs = dataStructs)

                differFileMap[pathString] = differFile
                files += differFile
            } catch (e: Exception) {
                println(e)
            }
        }

        return files
    }

    // TODO get the data structure from the file or cache while already got from language analyser
    private fun diffFileFromBlob(
        repository: Repository,
        blobId: ObjectId,
        pathString: String,
        extension: String
    ): Array<CodeDataStruct> {
        val fileName = File(pathString).name
        val content = repository.newObjectReader().use { objectReader ->
            String(objectReader.open(blobId).bytes, StandardCharsets.UTF_8)
        }
        return when (extension) {
            "kt" -> {
                val analyser = chapi.ast.kotlinast.KotlinAnalyser()
                analyser.analysis(content, fileName, AnalysisMode.Full).run {
                    DataStructures.map { it.apply { it.FilePath = pathString } }
                }.toTypedArray()
            }
            "java" -> {
                val analyser = chapi.ast.javaast.JavaAnalyser()
                val basicNodes = analyser.identBasicInfo(content, fileName).run {
                    DataStructures.map { ds -> ds.apply { ds.Imports = Imports } }
                }.toTypedArray()
                val classes = basicNodes.map { it.getClassFullName() }.toTypedArray()
                analyser.identFullInfo(content, fileName, classes, basicNodes).run {
                    DataStructures.map { ds -> ds.apply { ds.Imports = Imports } }
                }.toTypedArray()
            }
            else -> emptyArray()
        }
    }

    private fun Git.specifyBranch(branch: String): Git = apply {
        checkout().setName(branch).call()
    }
}
