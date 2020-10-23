package com.thoughtworks.archguard.report.domain.cohesion

import com.thoughtworks.archguard.report.domain.models.ClassVO
import java.nio.file.Path

data class ShotgunSurgery(val commitId: String,
                          val commitMessage: String,
                          val paths: List<Path>) {
    val clazzes: List<ClassVO>
        get() = paths.mapNotNull { ClassVO.create(it) }

    companion object {
        const val LIMIT = 8
    }
}
