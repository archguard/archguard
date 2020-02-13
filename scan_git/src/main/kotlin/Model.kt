package com.thoughtworks.archguard.git.scanner

data class GitRepository(val branch: String, val commits: List<Commit>)

data class Commit(val time: Int, val name: String, val committer: Committer, val changes: List<ChangeEntry>)

data class Committer(val name: String, val email: String)

data class ChangeEntry(var path: String, var mode: String)

//todo: 暂时使用了String
enum class ChangeMode {
    Add, Delete, Modify
}
