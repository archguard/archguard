package org.archguard.smell.define

import org.archguard.smell.BadSmellLevel

interface SmellThreshold {
    fun levelRanges(): Array<LongRange>
    fun smellLevel() : BadSmellLevel
}