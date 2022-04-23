package org.archguard.scanner.core.utils

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

object CoroutinesExtension {
    suspend fun <T, R> List<T>.asyncMap(transform: (T) -> R) =
        coroutineScope { map { async { transform(it) } }.awaitAll() }
}
