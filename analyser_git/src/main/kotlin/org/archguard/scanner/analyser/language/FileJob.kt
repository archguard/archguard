// based on [https://github.com/boyter/scc](https://github.com/boyter/scc) with MIT LICENSE.
// SPDX-License-Identifier: MIT OR Unlicense

package org.archguard.scanner.analyser.language

class FileJob(
    val lines: Int = 0,
    val language: String = "",
    val complexity: Int = 0,
    val blank: Int = 0,
    val code: Int = 0,
    val extension: String = "",
    val location: String = "",
    val fileName: String = "",
)

