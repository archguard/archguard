package org.archguard.meta

// just for fun
typealias Coin = List<String>

typealias OutputType = List<Any>

typealias Materials = List<Any>

/**
 * Slot is like an extension point, we design it for analysis pipeline, after analyser output data.
 * Slot is a data consumer after analyser analysis data.
 * can be use for:
 * - linter
 * - metrics
 * - custom api analysis
 **/
interface Slot {
    /**
     *
     */
    var material: Materials
    var outClass: String

    /**
     * The required data type for search in Element, for example: WebAPI require ContainResource
     */
    abstract fun ticket(): Coin

    /**
     * TODO: not impl
     * Is a slot can be working like a event-based architecture.
     * If a slot is flowable, it can use like a data streaming analysis which is one by one.
      */
    fun flowable(): Boolean = false

    /**
     * prepare some data, like prepare [org.archguard.rule.core.RuleSet]
     */
    abstract fun prepare(items: List<Any>): List<Any>

    /**
     * process data
     * examples: process [org.archguard.rule.core.RuleSet] to generate [org.archguard.rule.core.Issue]
     */
    abstract fun process(items: List<Any>): OutputType
}
