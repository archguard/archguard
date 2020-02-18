package com.thoughtworks.archguard.git.scanner

import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import javax.persistence.*

@Entity
data class CommitHistory(
        @Id val branch: String,
        @OneToMany @Cascade(CascadeType.ALL) val commits: List<Commit>)

@Entity
data class Commit(
        val time: Int,
        @Id val hash: String,
        @Embedded val committer: Committer,
        @ElementCollection val changes: List<ChangeEntry>)

@Embeddable
data class Committer(
        val name: String,
        val email: String)


//todo: mode 字段， 暂时使用了String
@Embeddable
data class ChangeEntry(
        var oldPath: String,
        var newPath: String,
        var mode: String)

