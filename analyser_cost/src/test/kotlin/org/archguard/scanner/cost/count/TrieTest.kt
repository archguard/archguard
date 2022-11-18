package org.archguard.scanner.cost.count

import org.archguard.scanner.cost.count.TokenType.*
import org.junit.jupiter.api.Test

class TrieTest {
    @Test
    fun BenchmarkCheckComplexity() {
        val matches = Trie()
        matches.insert(TComplexity, "for ")
        matches.insert(TComplexity, "for(")
        matches.insert(TComplexity, "if ")
        matches.insert(TComplexity, "if(")
        matches.insert(TComplexity, "switch ")
        matches.insert(TComplexity, "while ")
        matches.insert(TComplexity, "else ")
        matches.insert(TComplexity, "|| ")
        matches.insert(TComplexity, "&& ")
        matches.insert(TComplexity, "!= ")
        matches.insert(TComplexity, "== ")

        val content = "A little while ago, I passed my first year mark of working for Google. This also marked the ".toByteArray()

        for (i in 0..1000) {
            for (j in content.indices) {
                matches.match(content)
            }
        }
    }
}