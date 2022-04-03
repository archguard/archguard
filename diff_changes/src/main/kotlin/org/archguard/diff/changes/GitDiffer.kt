package org.archguard.diff.changes

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File

object GitDiffer {
    fun getRange(path: String, branch: String = "master", sinceRev: String, untilRev: String) {
        val repPath = File(path)
        val repository = FileRepositoryBuilder().findGitDir(repPath).build()
        val git = Git(repository).specifyBranch(branch)

        val since: ObjectId = git.repository.resolve(sinceRev)
        val until: ObjectId = git.repository.resolve(untilRev)
        for (commit in git.log().addRange(since, until).call()) {
            println(commit.shortMessage)
        }
    }

    private fun Git.specifyBranch(branch: String): Git {
        checkout().setName(branch).call()
        return this
    }

}