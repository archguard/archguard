package org.archguard.scanner.ctl.loader

import kotlinx.coroutines.runBlocking
import org.archguard.meta.Slot
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.context.AnalyserType
import org.archguard.scanner.core.context.Context
import org.archguard.scanner.core.diffchanges.DiffChangesAnalyser
import org.archguard.scanner.core.diffchanges.DiffChangesContext
import org.archguard.scanner.core.git.GitAnalyser
import org.archguard.scanner.core.git.GitContext
import org.archguard.scanner.core.sca.ScaAnalyser
import org.archguard.scanner.core.sca.ScaContext
import org.archguard.scanner.core.sourcecode.SourceCodeAnalyser
import org.archguard.scanner.core.sourcecode.SourceCodeContext
import org.archguard.scanner.core.utils.CoroutinesExtension.asyncMap
import org.archguard.scanner.ctl.command.ScannerCommand
import org.archguard.scanner.ctl.impl.CliDiffChangesContext
import org.archguard.scanner.ctl.impl.CliGitContext
import org.archguard.scanner.ctl.impl.CliScaContext
import org.archguard.scanner.ctl.impl.CliSourceCodeContext
import org.archguard.scanner.ctl.impl.OfficialAnalyserSpecs
import org.slf4j.LoggerFactory
import java.io.File

class AnalyserDispatcher {
    fun dispatch(command: ScannerCommand) {
        when (command.type) {
            AnalyserType.SOURCE_CODE -> SourceCodeWorker(command)
            AnalyserType.GIT -> GitWorker(command)
            AnalyserType.DIFF_CHANGES -> DiffChangesWorker(command)
            AnalyserType.SCA -> ScaWorker(command)
            AnalyserType.RULE -> RuleWorker(command)
            else -> TODO("not implemented yet")
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

private class SourceCodeWorker(override val command: ScannerCommand) : Worker<SourceCodeContext> {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    override val context = CliSourceCodeContext(
        client = command.buildClient(),
        path = command.path,
        language = command.language!!,
        features = command.features,
        slots = command.slots
    )

    val slotTypes: MutableMap<String, SourceCodeSlot> = mutableMapOf()

    override fun run(): Unit = runBlocking {
        val languageAnalyser = getOrInstall<SourceCodeAnalyser>(context.language)
        val ast = languageAnalyser.analyse(null) ?: return@runBlocking

        setupSlots()
        maybePlugSlot(ast)

        // TODO: support for multiple feature collections
        context.features.asyncMap {
            try {
                val data = getOrInstall<SourceCodeAnalyser>(it).analyse(ast)
                maybePlugSlot(data)
            } catch (e: Exception) {
                logger.error("Error while analysing feature: $it", e)
            }
        }
    }

    private fun maybePlugSlot(data: Any?) {
        if (data == null) return

        // for handle old versions plugin
        if (data !is List<*>) return

        val items = data as List<Any>
        if (items.isEmpty()) return

        val outputType = items[0]::class.java.name
        logger.info("found output type: $outputType")

        val slot = slotTypes[outputType] ?: return

        plugSlot(slot, items)
    }

    private fun plugSlot(slot: SourceCodeSlot, data: List<Any>) {
        logger.info("try plug slot for: ${slot.clz}")

        slot.clz.prepare(emptyList())
        val output = slot.clz.process(data)

        // todo: move api process in slot
        when (slot.define.slotType) {
            "rule" -> {
                File("slot.json").writeText(output.toString())
            }
        }
    }

    private fun setupSlots() {
        context.slots.filter {
            it.slotType == "rule"
        }.map {
            val slotInstance = AnalyserLoader.loadSlot(it)
            val coin = slotInstance.ticket()[0]
            slotTypes[coin] = SourceCodeSlot(it, slotInstance)
        }
    }
}

private class GitWorker(override val command: ScannerCommand) : Worker<GitContext> {
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

private class DiffChangesWorker(override val command: ScannerCommand) : Worker<DiffChangesContext> {
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

private class ScaWorker(override val command: ScannerCommand) : Worker<ScaContext> {
    override val context = CliScaContext(
        client = command.buildClient(),
        path = command.path,
        language = command.language!!,
    )

    override fun run() {
        getOrInstall<ScaAnalyser>(OfficialAnalyserSpecs.SCA).analyse()
    }
}

private class RuleWorker(override val command: ScannerCommand) : Worker<ScaContext> {
    override val context = CliScaContext(
        client = command.buildClient(),
        path = command.path,
        language = command.language!!,
    )

    override fun run() {
        getOrInstall<RuleAnalyser>(OfficialAnalyserSpecs.Rule).analyse()
    }
}
