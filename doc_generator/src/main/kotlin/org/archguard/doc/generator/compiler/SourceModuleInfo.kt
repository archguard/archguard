package org.archguard.doc.generator.compiler

import org.jetbrains.kotlin.analyzer.ModuleInfo
import org.jetbrains.kotlin.analyzer.common.CommonPlatformAnalyzerServices
import org.jetbrains.kotlin.descriptors.ModuleCapability
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.platform.CommonPlatforms
import org.jetbrains.kotlin.platform.TargetPlatform
import org.jetbrains.kotlin.resolve.PlatformDependentAnalyzerServices

class SourceModuleInfo(
    override val name: Name,
    override val capabilities: Map<ModuleCapability<*>, Any?>,
    private val dependOnOldBuiltIns: Boolean,
    override val analyzerServices: PlatformDependentAnalyzerServices = CommonPlatformAnalyzerServices,
    override val platform: TargetPlatform = CommonPlatforms.defaultCommonPlatform,
) : ModuleInfo {
    override fun dependencies() = listOf(this)

    override fun dependencyOnBuiltIns(): ModuleInfo.DependencyOnBuiltIns =
        if (dependOnOldBuiltIns) {
            ModuleInfo.DependencyOnBuiltIns.LAST
        } else {
            ModuleInfo.DependencyOnBuiltIns.NONE
        }
}