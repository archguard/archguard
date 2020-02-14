package com.thoughtworks.archguard.git.scanner

import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import javax.persistence.*

@Entity
class GitRepository() {
    @Id
    var branch: String? = null
    @OneToMany
    @Cascade(CascadeType.ALL)
    var commits: List<Commit>? = null

    constructor(branch: String, commits: List<Commit>) : this() {
        this.branch = branch
        this.commits = commits
    }
}

@Entity
data class Commit(val time: Int?, @Id val hash: String?, @Embedded val committer: Committer?, @ElementCollection val changes: List<ChangeEntry>?) {
    constructor() : this(null, null, null, null)
}


@Embeddable
data class Committer(val name: String?, val email: String?) {
    constructor() : this(null, null)
}

@Embeddable
data class ChangeEntry(var oldPath: String?, var newPath: String?, var mode: String?) {
    constructor() : this(null, null, null)
}

//todo: 暂时使用了String
enum class ChangeMode {
    Add, Delete, Modify
}
