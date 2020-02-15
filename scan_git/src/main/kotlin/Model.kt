package com.thoughtworks.archguard.git.scanner

import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import javax.persistence.*

@Entity
open class GitRepository(@Id val branch: String?, @OneToMany @Cascade(CascadeType.ALL) val commits: List<Commit>?) {
    constructor() : this(null, null)
}

@Entity
open class Commit(val time: Int?, @Id val hash: String?, @Embedded val committer: Committer?, @ElementCollection val changes: List<ChangeEntry>?) {
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
