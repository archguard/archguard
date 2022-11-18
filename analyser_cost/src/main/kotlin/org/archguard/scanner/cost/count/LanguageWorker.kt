package org.archguard.scanner.cost.count

import java.io.File
import java.security.MessageDigest
import kotlin.experimental.and

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

    fun countStates(fileJob: FileJob): FileJob? {
        val langFeatures = languageService.getLanguageFeature(fileJob.language) ?: return null

        if (langFeatures.complexity == null) langFeatures.complexity = Trie()
        if (langFeatures.singleLineComments == null) langFeatures.singleLineComments = Trie()
        if (langFeatures.multiLineComments == null) langFeatures.multiLineComments = Trie()
        if (langFeatures.strings == null) langFeatures.strings = Trie()
        if (langFeatures.tokens == null) langFeatures.tokens = Trie()

        val endPoint: Int = (fileJob.bytes - 1).toInt()
        var currentState: CodeState = CodeState.BLANK
        var endComments = byteArrayOf()
        var endString = byteArrayOf()

        var ignoreEscape = false;

        val first = checkBomSkip(fileJob)
        var index = first;
        while (index in first until fileJob.bytes.toInt()) {
            if (!isWhiteSpace(fileJob.content[index])) {
                println("$index, $currentState")
                when (currentState) {
                    CodeState.CODE -> {
                        val codeStateTransition = codeState(
                            fileJob,
                            index,
                            endPoint,
                            currentState,
                            endString,
                            endComments,
                            langFeatures,
                            fileJob.hash
                        )
                        index = codeStateTransition.index
                        currentState = codeStateTransition.state
                        endString = codeStateTransition.endString
                        endComments = codeStateTransition.endComments
                        ignoreEscape = codeStateTransition.ignoreEscape
                    }

                    CodeState.STRING -> {
                        val stringStateTransition = stringState(
                            fileJob,
                            index,
                            endPoint,
                            endString,
                            currentState,
                            ignoreEscape
                        )
                        index = stringStateTransition.index
                        currentState = stringStateTransition.state
                    }

                    CodeState.DOC_STRING -> {
                        val docStringStateTransition = docStringState(
                            fileJob,
                            index,
                            endPoint,
                            endString,
                            currentState,
                            langFeatures.strings!!,
                        )
                        index = docStringStateTransition.index
                        currentState = docStringStateTransition.state
                    }

                    CodeState.MULTICOMMENT, CodeState.MULTICOMMENT_CODE -> {
                        val commentStateTransition = commentState(
                            fileJob,
                            index,
                            endPoint,
                            currentState,
                            endComments,
                            endString,
                            langFeatures
                        )
                        index = commentStateTransition.index
                        currentState = commentStateTransition.state
                        endString = commentStateTransition.endString
                        endComments = commentStateTransition.endComments
                    }

                    CodeState.BLANK, CodeState.MULTICOMMENT_BLANK -> {
                        val blankStateTransition = blankState(
                            fileJob,
                            index,
                            endPoint,
                            currentState,
                            endComments,
                            endString,
                            langFeatures
                        )
                        index = blankStateTransition.index
                        currentState = blankStateTransition.state
                        endString = blankStateTransition.endString
                        endComments = blankStateTransition.endComments
                        ignoreEscape = blankStateTransition.ignoreEscape
                    }

                    else -> {}
                }
            }

            index += 1
        }

        return fileJob
    }

    var Duplicates = false
    fun codeState(
        fileJob: FileJob,
        index: Int,
        endPoint: Int,
        currentState: CodeState,
        endString: ByteArray,
        endComments: ByteArray,
        langFeatures: LanguageFeature,
        digest: MessageDigest
    ): CodeStateTransition {
        // Hacky fix to
        var endPoint = endPoint
        if (endPoint > fileJob.content.size) {
            endPoint--
        }

        var id = index;
        while (id in (index + 1) until endPoint) {
            val curByte = fileJob.content[id]

            if (curByte == '\n'.toByte()) {
                return CodeStateTransition(id, currentState, endString)
            }

            if (isBinary(id, curByte)) {
                fileJob.binary = true
                return CodeStateTransition(id, currentState, endString)
            }

            if (shouldProcess(curByte, langFeatures.processMask!!)) {
                if (Duplicates) {
                    // Technically this is wrong because we skip bytes so this is not a true
                    // hash of the file contents, but for duplicate files it shouldn't matter
                    // as both will skip the same way
                    val digestible = byteArrayOf(fileJob.content[index])
                    digest.update(digestible)
                }

                val rangeContent: ByteArray = fileJob.content.sliceArray(index until endPoint)
                val (tokenType, offsetJump, matchEndString) = langFeatures.tokens?.match(rangeContent)!!
                when (tokenType) {
                    TokenType.TString -> {
                        // If we are in string state then check what sort of string so we know if docstring OR ignoreescape string
                        val (i, ignoreEscape) = verifyIgnoreEscape(langFeatures, fileJob, index)

                        // It is safe to -1 here as to enter the code state we need to have
                        // transitioned from blank to here hence i should always be >= 1
                        // This check is to ensure we aren't in a character declaration
                        // TODO this should use language features
                        if (fileJob.content[i - 1] != '\\'.toByte()) {
                            return CodeStateTransition(i, CodeState.STRING, endString)
                        }

                        return CodeStateTransition(i, currentState, endString)
                    }

                    TokenType.TSlcomment -> {
                        return CodeStateTransition(id, CodeState.COMMENT_CODE, endString)
                    }

                    TokenType.TMlcomment -> {
                        if (langFeatures.nested == true || endComments.size == 0) {
                            endComments.plus(endString)
                            id += offsetJump - 1

                            return CodeStateTransition(id, CodeState.MULTICOMMENT_CODE, endString)
                        }
                    }

                    TokenType.TComplexity -> {
                        if (index == 0 || isWhitespace(fileJob.content[index - 1])) {
                            fileJob.complexity++
                        }
                    }
                }
            }
        }

        return CodeStateTransition(index, currentState, endString, endComments, false)
    }

    // // Check if this file is binary by checking for nul byte and if so bail out
    //// this is how GNU Grep, git and ripgrep check for binary files
    //func isBinary(index int, currentByte byte) bool {
    //	if index < 10000 && !DisableCheckBinary && currentByte == 0 {
    //		return true
    //	}
    //
    //	return false
    //}
    private fun isBinary(index: Int, currentByte: Byte): Boolean {
        return index < 10000 && currentByte == 0.toByte()
    }

    private fun shouldProcess(currentByte: Byte, processBytesMask: Byte): Boolean {
        return currentByte and processBytesMask == currentByte
    }

    fun commentState(
        fileJob: FileJob,
        index: Int,
        endPoint: Int,
        currentState: CodeState,
        endComments: ByteArray,
        endString: ByteArray,
        langFeatures: LanguageFeature,
    ): CodeStateTransition {
        var state = currentState;

        var id = index;
        while (id in (index + 1) until endPoint) {
            val curByte = fileJob.content[id]

            if (curByte == '\n'.code.toByte()) {
                return CodeStateTransition(
                    index = id,
                    state = currentState,
                    endString = endString,
                    endComments = endComments,
                )
            }

            val sliceComments = endComments.slice(0 until endComments.size - 1).toByteArray()
            if (checkForMatchSingle(curByte, id, endPoint, sliceComments, fileJob)) {
//                val byte: ByteArray = endComments.slice(0 until endComments.size - 1).toByteArray()
                val offsetJump = sliceComments.size
                endComments.slice(0 until endComments.size - 1).toByteArray()

                if (endComments.size == 0) {
                    // If we started as multiline code switch back to code so we count correctly
                    // IE i := 1 /* for the lols */
                    // TODO is that required? Might still be required to count correctly
                    if (currentState == CodeState.MULTICOMMENT_CODE) {
                        state = CodeState.CODE // TODO pointless to change here, just set S_MULTICOMMENT_BLANK
                    } else {
                        state = CodeState.MULTICOMMENT_BLANK
                    }
                }

                id += offsetJump - 1
                return CodeStateTransition(
                    index = id,
                    state = state,
                    endString = endString,
                    endComments = endComments,
                )
            }
            // Check if we are entering another multiline comment
            // This should come below check for match single as it speeds up processing
            if (langFeatures.nested == true || endComments.isEmpty()) {
                val (ok, offsetJump, endString) = langFeatures.multiLineComments?.match(fileJob.content.sliceArray(id until endPoint))!!
//                if (ok != 0) {
                endComments.plus(endString)
                id += offsetJump - 1

                return CodeStateTransition(
                    index = id,
                    state = currentState,
                    endString = endString,
                    endComments = endComments,
                )
//                }
            }

            id += 1
        }

        return CodeStateTransition(
            index = id,
            state = currentState,
            endString = endString,
            endComments = endComments,
        )
    }

    fun docStringState(
        fileJob: FileJob,
        index: Int,
        endPoint: Int,
        endString: ByteArray,
        currentState: CodeState,
        stringTrie: Trie
    ): CodeStateTransition {
        // Its not possible to enter this state without checking at least 1 byte so it is safe to check -1 here
        // without checking if it is out of bounds first
        for (i in index until endPoint) {
            val id = i

            if (fileJob.content[i] == '\n'.toByte()) {
                return CodeStateTransition(i, currentState)
            }

            if (fileJob.content[i - 1] != '\\'.toByte()) {
                val rangeContent: ByteArray = fileJob.content.sliceArray(index until endPoint)
                if (stringTrie.match(rangeContent) != null) {
                    // So we have hit end of docstring at this point in which case check if only whitespace characters till the next
                    // newline and if so we change to a comment otherwise to code
                    // need to start the loop after ending definition of docstring, therefore adding the length of the string to
                    // the index
                    for (j in id + endString.size until endPoint) {
                        if (fileJob.content[j] == '\n'.toByte()) {
                            return CodeStateTransition(i, CodeState.COMMENT)
                        }

                        if (!isWhitespace(fileJob.content[j])) {
                            return CodeStateTransition(i, CodeState.CODE)
                        }
                    }

                    return CodeStateTransition(i, CodeState.CODE)
                }
            }
        }

        return CodeStateTransition(index, currentState)
    }

    private fun isWhitespace(currentByte: Byte): Boolean {
        return currentByte == ' '.code.toByte() || currentByte == '\t'.code.toByte() || currentByte == '\n'.code.toByte() || currentByte == '\r'.code.toByte()
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

    fun blankState(
        fileJob: FileJob,
        index: Int,
        endPoint: Int,
        currentState: CodeState,
        endComments: ByteArray,
        endString: ByteArray,
        langFeatures: LanguageFeature
    ): CodeStateTransition {
        var state = currentState;
        val rangeContent: ByteArray = fileJob.content.sliceArray(index until endPoint)
        val (tokenType, offsetJump, matchEndString) = langFeatures.tokens?.match(rangeContent)!!

        when (tokenType) {
            TokenType.TMlcomment -> {
                if (langFeatures.nested == true || endComments.isEmpty()) {
                    return CodeStateTransition(
                        offsetJump - 1,
                        CodeState.MULTICOMMENT,
                        matchEndString,
                        endComments.plus(matchEndString),
                        false
                    )
                }
            }

            TokenType.TSlcomment -> {
                return CodeStateTransition(index, CodeState.COMMENT, matchEndString, endComments, false)
            }

            TokenType.TString -> {
                val (id, ignoreEscape) = verifyIgnoreEscape(langFeatures, fileJob, index)

                for (v in langFeatures.quotes!!) {
                    if (v.end == matchEndString.toString() && v.docString == true) {
                        return CodeStateTransition(
                            id,
                            CodeState.DOC_STRING,
                            matchEndString,
                            endComments,
                            ignoreEscape
                        )
                    }
                }

                return CodeStateTransition(id, CodeState.STRING, matchEndString, endComments, ignoreEscape)
            }

            TokenType.TComplexity -> {
                if (index == 0 || isWhiteSpace(fileJob.content[index - 1])) {
                    fileJob.complexity++
                }

                return CodeStateTransition(index, CodeState.CODE, matchEndString, endComments, false)
            }

            else -> {
                state = CodeState.CODE
            }
        }

        return CodeStateTransition(index, state, endString, endComments, false)
    }

    fun verifyIgnoreEscape(langFeatures: LanguageFeature, fileJob: FileJob, index: Int): Pair<Int, Boolean> {
        var ignoreEscape = false

        for (i in 0 until langFeatures.quotes!!.size) {
            if (langFeatures.quotes[i].docString == true || langFeatures.quotes[i].ignoreEscape == true) {
                var isMatch = true
                for (j in 0 until langFeatures.quotes[i].start.length) {
                    if (fileJob.content.size <= index + j || fileJob.content[index + j] != langFeatures.quotes[i].start[j].code.toByte()) {
                        isMatch = false
                        break
                    }
                }

                if (isMatch) {
                    ignoreEscape = true
                    index + langFeatures.quotes[i].start.length
                }
            }
        }

        return Pair(index, ignoreEscape)
    }

    private fun isWhiteSpace(byte: Byte): Boolean {
        return byte == ' '.code.toByte() || byte == '\t'.code.toByte() || byte == '\n'.code.toByte() || byte == '\r'.code.toByte()
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
            if (matches.isEmpty()) return false
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