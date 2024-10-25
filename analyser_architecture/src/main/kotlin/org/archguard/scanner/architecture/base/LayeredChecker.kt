package org.archguard.scanner.architecture.base

interface LayeredChecker {
    fun addToIdentify(str: String)
    fun canMarked(): Boolean
}