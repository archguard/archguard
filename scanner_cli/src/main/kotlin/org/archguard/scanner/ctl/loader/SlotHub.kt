package org.archguard.scanner.ctl.loader

import org.archguard.rule.core.Issue
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.context.Context
import org.slf4j.LoggerFactory

/**
 * TODO: in order to support for multiple feature collections, need to refactor to pub/sub mod
 * SlotHub
 */
class SlotHub(val context: Context) {
    private val slotInstanceByType: MutableMap<String, SourceCodeSlot> = mutableMapOf()
    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val outCount: Int = 0

    fun register(analyserSpecs: List<AnalyserSpec>) {
        analyserSpecs.filter {
            it.slotType == "rule"
        }.map {
            val slotInstance = AnalyserLoader.loadSlot(it)
            val coin = slotInstance.ticket()[0]

            slotInstanceByType[coin] = SourceCodeSlot(it, slotInstance)
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

        val slot = slotInstanceByType[outputType] ?: return

        plug(slot, items)
    }

    fun plug(slot: SourceCodeSlot, data: List<Any>) {
        logger.info("try plug slot for: ${slot.clz}")

        slot.clz.prepare(emptyList())
        val output = slot.clz.process(data)

        logger.info("done plug slot for: ${slot.clz}")

        // todo: move api process in slot
        when (slot.define.slotType) {
            "rule" -> {
                context.client.saveRuleIssues(output as List<Issue>)
            }
        }
    }
}