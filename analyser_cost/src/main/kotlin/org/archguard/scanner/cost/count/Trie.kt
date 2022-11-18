package org.archguard.scanner.cost.count

import kotlinx.serialization.Serializable


enum class TokenType {
    TString,
    TSlcomment,
    TMlcomment,
    TComplexity;

    inline fun <reified E : Enum<E>> fromInt(value: Int): E {
        return enumValues<E>().first { it.toString().toInt() == value }
    }

    fun toInt(): Byte {
        return (ordinal + 1).toByte()
    }
}

@Serializable
class Trie(
    var type: TokenType = TokenType.TString,
    var close: ByteArray = byteArrayOf(),
    var table: HashSet<Trie>? = hashSetOf(),
) {
    fun insert(tokenType: TokenType, token: ByteArray) {
        var node: Trie? = this

        for (c in token) {
            if (node?.table?.find { it.type == tokenType && it.close.contentEquals(byteArrayOf(c)) } == null) {
                node?.table?.add(Trie(tokenType, byteArrayOf(c)))
            }
            node = node?.table?.find { it.type == tokenType && it.close.contentEquals(byteArrayOf(c)) }
        }
        node?.type = tokenType
    }

    fun insertClose(tokenType: TokenType, openToken: ByteArray, closeToken: ByteArray) {
        var node: Trie? = this

        for (c in openToken) {
            if (node?.table?.find { it.type == tokenType && it.close.contentEquals(byteArrayOf(c)) } == null) {
                node?.table?.add(Trie(tokenType, byteArrayOf(c)))
            }
            node = node?.table?.find { it.type == tokenType && it.close.contentEquals(byteArrayOf(c)) }
        }
        node?.type = tokenType
        node?.close = closeToken
    }

    fun match(token: ByteArray): TrieMatch {
        var node: Trie = this
        for ((i, c) in token.withIndex()) {
            if (node.table?.find { it.type.toInt() == c } == null) {
                return TrieMatch(node.type, i, node.close)
            }
            node = node.table?.find { it.type.toInt() == c }!!
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
    val tokenType: TokenType,
    val offsetJump: Int,
    val endString: ByteArray,
)