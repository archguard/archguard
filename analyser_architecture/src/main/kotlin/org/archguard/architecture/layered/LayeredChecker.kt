package org.archguard.architecture.layered

interface LayeredChecker {
    fun addToIdentify(str: String)
    fun canMarked(): Boolean
}