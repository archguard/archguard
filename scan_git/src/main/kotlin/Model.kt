package com.thoughtworks.archguard.git.scanner

import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import javax.persistence.*

@Entity
open class CommitHistory(
        @Id val branch: String? = null,
        @OneToMany @Cascade(CascadeType.ALL) val commits: List<Commit>? = null)

@Entity
open class Commit(
        val time: Int? = null,
        @Id val hash: String? = null,
        @Embedded val committer: Committer? = null,
        @ElementCollection val changes: List<ChangeEntry>? = null)

@Embeddable
open class Committer(
        val name: String? = null,
        val email: String? = null)


//todo: mode 字段， 暂时使用了String
@Embeddable
open class ChangeEntry(
        var oldPath: String? = null,
        var newPath: String? = null,
        var mode: String? = null)

