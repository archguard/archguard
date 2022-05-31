package org.archguard.meta

/**
 * Slot Type is design for plug slot in source code analysis
 * Slot is a data consumer after analyser analysis data.
 * can be use for:
 * - linter
 * - metrics
 * - custom api analysis
 **/
interface Slot {
    abstract fun requires(): List<String>
    abstract fun process(items: List<Any>): List<Any>
}
