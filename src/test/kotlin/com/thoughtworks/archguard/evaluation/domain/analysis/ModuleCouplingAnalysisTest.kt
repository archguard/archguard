package com.thoughtworks.archguard.evaluation.domain.analysis

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ModuleCouplingAnalysisTest {
    
    @Test
    internal fun test1() {
        assertEquals("h", getKeyLike(setOf("h", "e"), "hello"))
        assertEquals("h", getKeyLike(setOf("a", "e", "h", "h", "e"), "hello"))
        assertEquals(null, getKeyLike(setOf("h", "e"), "no"))
        assertEquals(null, getKeyLike(emptySet(), "no"))
        
        assertEquals("h", getKeyLike1(setOf("h", "e"), "hello"))
        assertEquals("h", getKeyLike1(setOf("a", "e", "h", "h", "e"), "hello"))
        assertEquals(null, getKeyLike1(setOf("h", "e"), "no"))
        assertEquals(null, getKeyLike1(emptySet(), "no"))
    }
    
    private fun getKeyLike1(keys: Set<String>, key: String): String? {
        return keys.find { key.startsWith(it) }
    }

    private fun getKeyLike(keys: Set<String>, key: String): String? {
        keys.forEach {
            if (key.startsWith(it)) {
                return it
            }
        }
        return null
    }

}