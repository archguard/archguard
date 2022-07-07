package org.archguard.domain.insight

class InsightsParser {
    fun tokenize(text: String) {
        val length = text.length;
        var index = 0;

        while (index < length) {
            val c = text[index];
            when (c) {
                ' ' -> {
                    index++;
                }
                else -> {
                    index++;
                }
            }
        }
    }
}