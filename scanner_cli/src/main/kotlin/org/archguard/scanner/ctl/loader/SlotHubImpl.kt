package org.archguard.scanner.ctl.loader

import org.archguard.meta.OutputType
import org.archguard.meta.Slot
import org.archguard.rule.core.Issue
import org.archguard.scanner.core.SlotSpec
import org.archguard.scanner.core.context.Context
import org.archguard.scanner.core.context.SlotHub
import org.slf4j.LoggerFactory

/**
 * TODO: in order to support for multiple feature collections, need to refactor to pub/sub mod
 * SlotHubImpl
 */
class SlotHubImpl(val context: Context) : SlotHub {
    private val slotInstanceByType: MutableMap<String, SourceCodeSlot> = mutableMapOf()
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun register(specs: List<SlotSpec>) {
        specs.forEach {
            try {
                val slotInstance = AnalyserLoader.loadSlot(it)
                registerSlotBySpec(it, slotInstance)
            } catch (e: Exception) {
                logger.warn(e.message.toString())
            }
        }
    }

    // todo: support for multiple tickets
    private fun registerSlotBySpec(spec: SlotSpec, instance: Slot) {
        val coin = instance.ticket()[0]

        logger.info("register slot: ${spec.identifier}")
        slotInstanceByType[coin] = SourceCodeSlot(spec, instance)
    }

    private fun lookupSlot(outputType: String): SourceCodeSlot? {
        // todo: find a better way to save last type data
        return slotInstanceByType[outputType]
    }

    fun consumer(data: Any?) {
        if (data == null) return

        // for handle old versions plugin
        if (data !is List<*>) return

        val items = data as List<Any>
        if (items.isEmpty()) return

        val outputType = items[0]::class.java.name
        logger.info("found output type: $outputType")

        val slot = lookupSlot(outputType) ?: return

        executeWithData(slot, items)
    }

    private fun executeWithData(slot: SourceCodeSlot, data: List<Any>) {
        val output = execute(slot, data)

        maybeOutputCanConsume(output, data)

        handleForOfficialSlot(slot, output)
    }

    private fun execute(slot: SourceCodeSlot, data: List<Any>): OutputType {
        logger.info("try plug slot for: ${slot.clz}")

        slot.clz.prepare(emptyList())
        val output = slot.clz.process(data)

        logger.info("done plug slot for: ${slot.clz}")
        return output
    }

    private fun handleForOfficialSlot(slot: SourceCodeSlot, output: OutputType) {
        when (slot.define.slotType) {
            "rule" -> {
                context.client.saveRuleIssues(output as List<Issue>)
            }
        }
    }

    private fun maybeOutputCanConsume(output: OutputType, data: List<Any>) {
        if (output.isEmpty()) return

        val isSameType = output[0].javaClass.name == data[0].javaClass.name
        if (isSameType) return

        consumer(output)
    }
}