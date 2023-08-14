package org.archguard.scanner.ctl.loader

import kotlinx.coroutines.runBlocking
import org.archguard.meta.Slot
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context
import org.archguard.scanner.core.diffchanges.DiffChangesAnalyser
import org.archguard.scanner.core.diffchanges.DiffChangesContext
import org.archguard.scanner.core.estimate.EstimateAnalyser
import org.archguard.scanner.core.estimate.EstimateContext
import org.archguard.scanner.core.git.GitAnalyser
import org.archguard.scanner.core.git.GitContext
import org.archguard.scanner.core.openapi.OpenApiAnalyser
import org.archguard.scanner.core.sca.ScaAnalyser
import org.archguard.scanner.core.sca.ScaContext
import org.archguard.scanner.core.sourcecode.SourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.archguard.scanner.core.utils.CoroutinesExtension.asyncMap
import org.archguard.scanner.ctl.command.ScannerCommand
import org.archguard.scanner.ctl.impl.*
import org.slf4j.LoggerFactory

class AnalyserDispatcher {
    fun dispatch(command: ScannerCommand) {
        when (command.type) {
            AnalyserType.SOURCE_CODE -> SourceCodeWorker(command)
            AnalyserType.GIT -> GitWorker(command)
            AnalyserType.DIFF_CHANGES -> DiffChangesWorker(command)
            AnalyserType.SCA -> ScaWorker(command)
            AnalyserType.RULE -> RuleWorker(command)
            AnalyserType.ESTIMATE -> EstimateWorker(command)
            AnalyserType.ARCHITECTURE -> TODO()
            AnalyserType.OPEN_API -> OpenApiWorker(command)
        }.run()
    }
}

private interface Worker<T : Context> {
    val command: ScannerCommand
    val context: T
    fun run()

    fun <T> getOrInstall(spec: OfficialAnalyserSpecs): T = getOrInstall(spec.name.lowercase())

    @Suppress("UNCHECKED_CAST")
    fun <T> getOrInstall(identifier: String): T = AnalyserLoader.load(context, command.getAnalyserSpec(identifier)) as T
}

data class SourceCodeSlot(
    val define: AnalyserSpec,
    val clz: Slot,
)

class SourceCodeWorker(override val command: ScannerCommand) : Worker<SourceCodeContext> {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    override val context = CliSourceCodeContext(
        client = command.buildClient(),
        path = command.path,
        language = command.language!!,
        features = command.features,
    )

    private val slotHub = SlotHubImpl(context)

    init {
        slotHub.register(command.slots)
    }

    override fun run(): Unit = runBlocking {
        val languageAnalyser = getOrInstall<SourceCodeAnalyser>(context.language)
        val ast = languageAnalyser.analyse(null) ?: return@runBlocking

        slotHub.consumer(ast)

        context.features.asyncMap {
            try {
                val data = getOrInstall<SourceCodeAnalyser>(it).analyse(ast)
                slotHub.consumer(data)
            } catch (e: Exception) {
                logger.error("Error while analysing feature: $it", e)
            }
        }
    }
}

class GitWorker(override val command: ScannerCommand) : Worker<GitContext> {
    override val context = CliGitContext(
        client = command.buildClient(),
        path = command.path,
        repoId = command.repoId!!,
        branch = command.branch,
        startedAt = command.startedAt,
    )

    override fun run() {
        getOrInstall<GitAnalyser>(OfficialAnalyserSpecs.GIT).analyse()
    }
}

class DiffChangesWorker(override val command: ScannerCommand) : Worker<DiffChangesContext> {
    override val context = CliDiffChangesContext(
        client = command.buildClient(),
        path = command.path,
        branch = command.branch,
        since = command.since!!,
        until = command.until!!,
        depth = command.depth,
    )

    override fun run() {
        getOrInstall<DiffChangesAnalyser>(OfficialAnalyserSpecs.DIFF_CHANGES).analyse()
    }
}

class ScaWorker(override val command: ScannerCommand) : Worker<ScaContext> {
    override val context = CliScaContext(
        client = command.buildClient(),
        path = command.path,
        language = command.language!!,
    )

    override fun run() {
        getOrInstall<ScaAnalyser>(OfficialAnalyserSpecs.SCA).analyse()
    }
}

class RuleWorker(override val command: ScannerCommand) : Worker<ScaContext> {
    override val context = CliScaContext(
        client = command.buildClient(),
        path = command.path,
        language = command.language!!,
    )

    override fun run() {
        getOrInstall<RuleAnalyser>(OfficialAnalyserSpecs.RULE).analyse()
    }
}

class EstimateWorker(override val command: ScannerCommand) : Worker<EstimateContext> {
    override val context = CliEstimateContext(
        client = command.buildClient(),
        path = command.path,
        branch = command.branch
    )

    override fun run() {
        getOrInstall<EstimateAnalyser>(OfficialAnalyserSpecs.ESTIMATE).analyse()
    }
}

class OpenApiWorker(override val command: ScannerCommand) : Worker<EstimateContext> {
    override val context = CliEstimateContext(
        client = command.buildClient(),
        path = command.path,
        branch = command.branch
    )

    override fun run() {
        getOrInstall<OpenApiAnalyser>(OfficialAnalyserSpecs.OPEN_API).analyse()
    }
}