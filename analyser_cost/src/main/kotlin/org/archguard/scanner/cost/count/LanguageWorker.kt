package org.archguard.scanner.cost.count

import java.io.File

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

// // ByteOrderMarks are taken from https://en.wikipedia.org/wiki/Byte_order_mark#Byte_order_marks_by_encoding
//// These indicate that we cannot count the file correctly so we can at least warn the user
//var ByteOrderMarks = [][]byte{
//	{254, 255},            // UTF-16 BE
//	{255, 254},            // UTF-16 LE
//	{0, 0, 254, 255},      // UTF-32 BE
//	{255, 254, 0, 0},      // UTF-32 LE
//	{43, 47, 118, 56},     // UTF-7
//	{43, 47, 118, 57},     // UTF-7
//	{43, 47, 118, 43},     // UTF-7
//	{43, 47, 118, 47},     // UTF-7
//	{43, 47, 118, 56, 45}, // UTF-7
//	{247, 100, 76},        // UTF-1
//	{221, 115, 102, 115},  // UTF-EBCDIC
//	{14, 254, 255},        // SCSU
//	{251, 238, 40},        // BOCU-1
//	{132, 49, 149, 51},    // GB-18030
//}
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
    }
}