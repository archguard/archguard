package org.archguard.architecture

import chapi.domain.core.CodeDataStruct
import org.archguard.architecture.core.Workspace
import org.archguard.architecture.detect.ArchitectureDetect
import org.archguard.architecture.detect.PotentialExecArch
import org.archguard.meta.Coin
import org.archguard.meta.Materials
import org.archguard.meta.OutputType
import org.archguard.meta.Slot
import org.archguard.scanner.core.git.GitLogs
import org.archguard.scanner.core.sca.PackageDependencies
import org.archguard.scanner.core.sourcecode.ContainerService

class ArchitectureSlot : Slot {
    override var material: Materials = listOf()
    override var outClass: String = PotentialExecArch.Companion::class.java.name
    private val workspace: Workspace = Workspace();

    private val dsName = CodeDataStruct::class.java.name
    private val pkgName = PackageDependencies::class.java.name
    private val csName = ContainerService::class.java.name
    private val glName = GitLogs::class.java.name

    override fun ticket(): Coin {
        return listOf(dsName, pkgName, csName, glName)
    }

    override fun prepare(items: List<Any>): List<Any> {
        TODO("Not yet implemented")
    }

    override fun process(items: List<Any>): OutputType {
        return listOf(ArchitectureDetect().identPotential(this.workspace))
    }
}