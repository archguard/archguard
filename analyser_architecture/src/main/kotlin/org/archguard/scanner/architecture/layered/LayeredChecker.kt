package org.archguard.scanner.architecture.layered

interface LayeredChecker {
    fun addToIdentify(str: String)
    fun canMarked(): Boolean
}