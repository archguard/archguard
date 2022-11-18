package org.archguard.scanner.cost.count

import java.io.File

class CodeStateTransition(
    val index: Int = 0,
    val state: CodeState = CodeState.BLANK,
    val endString: ByteArray = byteArrayOf(),
    val endComments: ByteArray = byteArrayOf(),
    val ignoreEscape: Boolean = false,
)

enum class CodeState {
    BLANK,
    CODE,
    COMMENT,
    COMMENT_CODE,
    MULTICOMMENT,
    MULTICOMMENT_CODE,
    MULTICOMMENT_BLANK,
    STRING,
    DOC_STRING;

    fun int(): Int {
        return ordinal + 1
    }
}

val byteOrderMarks = arrayOf(
    byteArrayOf(254.toByte(), 255.toByte()), // UTF-16 BE
    byteArrayOf(255.toByte(), 254.toByte()), // UTF-16 LE
    byteArrayOf(0, 0, 254.toByte(), 255.toByte()), // UTF-32 BE
    byteArrayOf(255.toByte(), 254.toByte(), 0, 0), // UTF-32 LE
    byteArrayOf(43, 47, 118, 56), // UTF-7
    byteArrayOf(43, 47, 118, 57), // UTF-7
    byteArrayOf(43, 47, 118, 43), // UTF-7
    byteArrayOf(43, 47, 118, 47), // UTF-7
    byteArrayOf(43, 47, 118, 56, 45), // UTF-7
    byteArrayOf(247.toByte(), 100, 76), // UTF-1
    byteArrayOf(221.toByte(), 115, 102, 115), // UTF-EBCDIC
    byteArrayOf(14, 254.toByte(), 255.toByte()), // SCSU
    byteArrayOf(251.toByte(), 238.toByte(), 40), // BOCU-1
    byteArrayOf(132.toByte(), 49, 149.toByte(), 51), // GB-18030
)

class LanguageWorker {
    val languageService: LanguageService = LanguageService()
    fun prepare(file: File): FileJob {
        // read file
        val fileContent = file.readBytes()
        val languages = languageService.detectLanguages(file.name)

        return FileJob(
            content = fileContent,
            possibleLanguages = languages
        )
    }

    fun countStates(fileJob: FileJob) {
        val langFeatures = languageService.getLanguageFeature(fileJob.language) ?: return

        if (langFeatures.complexity == null) langFeatures.complexity = Trie()
        if (langFeatures.singleLineComments == null) langFeatures.singleLineComments = Trie()
        if (langFeatures.multiLineComments == null) langFeatures.multiLineComments = Trie()
        if (langFeatures.strings == null) langFeatures.strings = Trie()
        if (langFeatures.tokens == null) langFeatures.tokens = Trie()

        var endPoint = fileJob.bytes - 1
        var currentState: CodeState = CodeState.BLANK
        val endComments = mutableListOf<ByteArray>()
        var endString = byteArrayOf()

        var ignoreEscape = false;

        // for index := checkBomSkip(fileJob); index < int(fileJob.Bytes); index++ { }
        for (index in checkBomSkip(fileJob) until (fileJob.bytes).toInt()) {
            if (!isWhiteSpace(fileJob.content[index])) {
                when (currentState) {
                    CodeState.CODE -> {

                    }

                    else -> {

                    }
                }

            }
        }
    }

    fun stringState(
        fileJob: FileJob,
        index: Int,
        endPoint: Int,
        endString: ByteArray,
        currentState: CodeState,
        ignoreEscape: Boolean
    ): CodeStateTransition {
        var id = index
        // It's not possible to enter this state without checking at least 1 byte so it is safe to check -1 here
        // without checking if it is out of bounds first
        for (i in id until endPoint) {
            id = i

            // If we hit a newline, return because we want to count the stats but keep
            // the current state, so we end up back in this loop when the outer
            // one calls again
            if (fileJob.content[i] == '\n'.code.toByte()) {
                return CodeStateTransition(i, currentState)
            }

            // If we are in a literal string we want to ignore the \ check OR we aren't checking for special ones
            if (ignoreEscape || fileJob.content[i - 1] != '\\'.code.toByte()) {
                if (checkForMatchSingle(fileJob.content[i], id, endPoint, endString, fileJob)) {
                    return CodeStateTransition(i, CodeState.CODE)
                }
            }
        }

        return CodeStateTransition(index = id, state = currentState)
    }

    private fun isWhiteSpace(byte: Byte): Boolean {
        return byte != ' '.toByte() && byte != '\t'.toByte() && byte != '\n'.toByte() && byte != '\r'.toByte()
    }

    companion object {
        fun checkBomSkip(fileJob: FileJob): Int {
            // UTF-8 BOM which if detected we should skip the BOM as we can then count correctly
            // []byte is UTF-8 BOM taken from https://en.wikipedia.org/wiki/Byte_order_mark#Byte_order_marks_by_encoding
            if (fileJob.content.contentEquals(byteArrayOf(239.toByte(), 187.toByte(), 191.toByte()))) {
                return 3
            }

            // If we have one of the other BOM then we might not be able to count correctly so if verbose let the user know
            for (v in byteOrderMarks) {
                if (fileJob.content.contentEquals(v)) {
                    println("BOM found for file ${fileJob.filename} indicating it is not ASCII/UTF-8 and may be counted incorrectly or ignored as a binary file")
                }
            }

            return 0
        }

        fun checkForMatchSingle(
            currentByte: Byte,
            index: Int,
            endPoint: Int,
            matches: ByteArray,
            fileJob: FileJob
        ): Boolean {
            var potentialMatch = true
            if (currentByte == matches[0]) {
                for (j in matches.indices) {
                    if (index + j >= endPoint || matches[j] != fileJob.content[index + j]) {
                        potentialMatch = false
                        break
                    }
                }

                if (potentialMatch) {
                    return true
                }
            }

            return false
        }
    }
}