package org.archguard.meta

/*
 * Slot Type is design for slot in source code analysis
 */
interface Slot {
    abstract fun requires(): List<String>
}
