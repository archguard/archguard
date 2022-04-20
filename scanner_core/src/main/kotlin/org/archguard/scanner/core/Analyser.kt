package org.archguard.scanner.core

import org.archguard.scanner.core.context.Context

interface Analyser<C : Context> {
    val context: C
}
