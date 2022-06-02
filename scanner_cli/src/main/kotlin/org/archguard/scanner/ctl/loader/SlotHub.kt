package org.archguard.scanner.ctl.loader

import org.archguard.rule.core.Issue
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.context.Context
import org.slf4j.LoggerFactory

class SlotHub(val context: Context) {
    val slotTypes: MutableMap<String, SourceCodeSlot> = mutableMapOf()
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun register(analyserSpecs: List<AnalyserSpec>) {
        analyserSpecs.filter {
            it.slotType == "rule"
        }.map {
            val slotInstance = AnalyserLoader.loadSlot(it)
            val coin = slotInstance.ticket()[0]
            slotTypes[coin] = SourceCodeSlot(it, slotInstance)
        }
    }

    fun maybePlugSlot(data: Any?) {
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

    fun plugSlot(slot: SourceCodeSlot, data: List<Any>) {
        logger.info("try plug slot for: ${slot.clz}")

        slot.clz.prepare(emptyList())
        val output = slot.clz.process(data)

        // todo: move api process in slot
        when (slot.define.slotType) {
            "rule" -> {
                context.client.saveRuleIssues(output as List<Issue>)
            }
        }
    }
}