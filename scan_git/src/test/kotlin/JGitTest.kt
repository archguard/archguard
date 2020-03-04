import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.diff.DiffFormatter
import org.eclipse.jgit.diff.RawTextComparator
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.treewalk.CanonicalTreeParser
import org.eclipse.jgit.treewalk.TreeWalk
import org.eclipse.jgit.util.io.DisabledOutputStream
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.function.Consumer

class JGitTest {

    private var repository: Repository? = null

    @BeforeEach
    @Throws(IOException::class)
    fun setUp() {
        repository = FileRepositoryBuilder()
                .findGitDir(File("test_data"))
                .build()
    }


    /*最终采用这种方式*/
    @Test
    @Throws(GitAPIException::class, IOException::class)
    fun diffFormat() {
        Git(repository).use { git ->
            val formatter = DiffFormatter(DisabledOutputStream.INSTANCE)
            formatter.setRepository(repository)
            formatter.setDiffComparator(RawTextComparator.DEFAULT)
            formatter.isDetectRenames = true
            var count = 0
            for (commit in git.log().call()) {
                count++
                println("commit:" + commit.name + ", message:" + commit.fullMessage)
                val parent = if (commit.parentCount != 0) commit.getParent(0) else null
                val diffEntries = formatter.scan(parent?.tree, commit.tree)
                diffEntries.forEach(Consumer { e: DiffEntry -> println("changeType:=" + e.changeType.name + ",newID=" + e.newId + ",newPath=" + e.newPath) })
            }
            println("count = $count")
        }
    }


    /*这是第二种方式，也可以*/
    @Test
    @Throws(GitAPIException::class, IOException::class)
    fun canonicalTreeParserTest() {
        Git(repository).use { git ->
            repository!!.newObjectReader().use { reader ->
                val oldTreeIter = CanonicalTreeParser()
                val newTreeIter = CanonicalTreeParser()
                for (commit in git.log().call()) {
                    println("commit:" + commit.name + ", message:" + commit.fullMessage)
                    val parent = if (commit.parentCount != 0) commit.getParent(0) else null
                    if (parent != null) oldTreeIter.reset(reader, parent.tree)
                    newTreeIter.reset(reader, commit.tree)
                    val diffEntries = git.diff().setNewTree(newTreeIter).setOldTree(oldTreeIter).call()
                    diffEntries.forEach(Consumer { e: DiffEntry -> println("changeType:=" + e.changeType.name + ",newID=" + e.newId + ",newPath=" + e.newPath) })
                }
            }
        }
    }

    /*not work，因为会包含一个所有的文件，不仅仅是变更的文件*/
    @Test
    @Throws(GitAPIException::class, IOException::class)
    fun testTreeWalk() {
        Git(repository).use { git ->
            TreeWalk(repository).use { treeWalk ->
                val revCommits = git.log().call()
                for (commit in revCommits) {
                    treeWalk.reset(commit.tree)
                    println("commit:" + commit.name + ", message:" + commit.fullMessage)
                    while (treeWalk.next()) {
                        val fileMode = treeWalk.fileMode
                        println("fileMode = $fileMode")
                        val pathString = treeWalk.pathString
                        println("pathString = $pathString")
                    }
                }
            }
        }
    }


    /** @see https://stackoverflow.com/questions/45793800/jgit-read-the-content-of-a-file-at-a-commit-in-a-branch*/
    @Test
    internal fun getContent_for_path_in_aCommit() {
        Git(repository).use { git ->
            git.log().call().forEach { revCommit ->
                println(revCommit.fullMessage + "++++++++++++++")
                TreeWalk.forPath(repository, "b", revCommit.tree).use { treeWalk ->
                    if (treeWalk != null) {
                        val objectId = treeWalk.getObjectId(0)
                        repository!!.newObjectReader().use { objectReader ->
                            val bytes = objectReader.open(objectId).bytes
                            println(String(bytes, StandardCharsets.UTF_8))
                        }
                    }
                }
            }
        }
    }


}