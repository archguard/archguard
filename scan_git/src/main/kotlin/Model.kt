package com.thoughtworks.archguard.git.scanner

import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import javax.persistence.*

@Entity
class CommitHistory(
        @Id val branch: String? = null,
        @OneToMany @Cascade(CascadeType.ALL) val commits: List<Commit>? = null)

@Entity
class Commit(
        val time: Int? = null,
        @Id val hash: String? = null,
        @Embedded val committer: Committer? = null,
        @ElementCollection val changes: List<ChangeEntry>? = null)

@Embeddable
data class Committer(
        val name: String? = null,
        val email: String? = null)

@Embeddable
data class ChangeEntry(
        var oldPath: String? = null,
        var newPath: String? = null,
        var mode: String? = null)

//todo: 暂时使用了String
enum class ChangeMode {
    Add, Delete, Modify
}
