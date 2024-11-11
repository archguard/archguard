package org.archguard.architecture.tokenizer

interface Tokenizer {
    fun tokenize(input: String): List<String>

    fun trim(array: MutableList<String>): List<String> {
        while (array.last() == "") {
            array.removeAt(array.lastIndex)
        }

        while (array.first() == "") {
            array.removeAt(0)
        }

        return array
    }
}