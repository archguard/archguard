package org.archguard.scanner.cost.count

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

class LanguageWorker {

}