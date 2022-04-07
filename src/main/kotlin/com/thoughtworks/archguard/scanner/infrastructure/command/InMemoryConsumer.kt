/*
 * Copyright 2022 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thoughtworks.archguard.scanner.infrastructure.command

import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class InMemoryConsumer : StreamConsumer {
    private val lines: Queue<String> = ConcurrentLinkedQueue()
    override fun consumeLine(line: String) {
        try {
            lines.add(line)
        } catch (e: RuntimeException) {
            LOG.error("Problem consuming line [{}]", line, e)
        }
    }

    fun asList(): List<String> {
        return ArrayList(lines)
    }

    operator fun contains(message: String?): Boolean {
        return toString().contains(message!!)
    }

    override fun toString(): String {
        return asList().joinToString("\n")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(InMemoryConsumer::class.java)
    }
}