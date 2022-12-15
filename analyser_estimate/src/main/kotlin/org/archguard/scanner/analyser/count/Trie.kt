package org.archguard.scanner.analyser.count

import kotlinx.serialization.Serializable

enum class TokenType {
    TString,
    TSlcomment,
    TMlcomment,
    TComplexity;
}

@Serializable
class Trie(
    var type: TokenType? = null,
    var close: ByteArray = byteArrayOf(),
    var table: HashMap<Byte, Trie> = HashMap()
) {
    fun insert(tokenType: TokenType, token: ByteArray) {
        var node: Trie = this

        for (c in token) {
            if (node.table[c] == null) {
                node.table[c] = Trie()
            }

            node = node.table[c]!!
        }
        node.type = tokenType
    }

    fun insertClose(tokenType: TokenType, openToken: ByteArray, closeToken: ByteArray) {
        var node: Trie = this

        for (c in openToken) {
            if (node.table[c] == null) {
                node.table[c] = Trie()
            }

            node = node.table[c]!!
        }

        node.type = tokenType
        node.close = closeToken
    }

    fun match(token: ByteArray): TrieMatch {
        var node: Trie = this
        for ((index, c) in token.withIndex()) {
            if (node.table[c] == null) {
                return TrieMatch(node.type, index, node.close)
            }

            node = node.table[c]!!
        }

        return TrieMatch(node.type, token.size, node.close)
    }

    fun insert(tokenType: TokenType, token: String) {
        insert(tokenType, token.toByteArray())
    }

    fun insertClose(tokenType: TokenType, openToken: String, closeToken: String) {
        insertClose(tokenType, openToken.toByteArray(), closeToken.toByteArray())
    }
}

data class TrieMatch(
    val tokenType: TokenType?,
    val offsetJump: Int,
    val endString: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TrieMatch) return false

        if (tokenType != other.tokenType) return false
        if (offsetJump != other.offsetJump) return false
        if (!endString.contentEquals(other.endString)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tokenType.hashCode()
        result = 31 * result + offsetJump
        result = 31 * result + endString.contentHashCode()
        return result
    }
}