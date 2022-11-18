package org.archguard.scanner.cost.count

import io.kotest.matchers.shouldBe
import org.archguard.scanner.cost.count.TokenType.*
import org.junit.jupiter.api.Test

class TrieTest {
    @Test
    fun simple() {
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

        matches.match("for ".toByteArray()).tokenType shouldBe TComplexity
        matches.match("for ()".toByteArray()).tokenType shouldBe TComplexity
    }

    @Test
    fun comments() {
        val matches = Trie()
        matches.insert(TSlcomment, "//");
        matches.insert(TMlcomment, "/*");

        val content = "//".toByteArray()

        matches.match(content).shouldBe(TrieMatch(TSlcomment, 2, byteArrayOf()))
    }
}