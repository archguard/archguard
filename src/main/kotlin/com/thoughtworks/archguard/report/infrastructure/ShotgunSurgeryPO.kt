package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.cohesion.ShotgunSurgery
import java.nio.file.Paths

class ShotgunSurgeryPO(val commitId: String, val commitMessage: String, val oldPath: String, val newPath: String) {

    companion object {
        fun from(shotgunSurgery: Map<String, List<ShotgunSurgeryPO>>): List<ShotgunSurgery> {
            val result = ArrayList<ShotgunSurgery>();
            for (entry in shotgunSurgery) {
                result.add(ShotgunSurgery(entry.key,
                        entry.value[0].commitMessage,
                        entry.value.map { if (it.newPath.equals("/dev/null")) it.oldPath else it.newPath }
                                .map { Paths.get(it) }))
            }
            return result;
        }
    }
}