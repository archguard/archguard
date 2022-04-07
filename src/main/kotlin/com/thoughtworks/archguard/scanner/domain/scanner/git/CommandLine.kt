package com.thoughtworks.archguard.scanner.domain.scanner.git

import java.util.function.Consumer

class CommandLine(command: String) {
    private lateinit var encoding: String
    private var secrets: List<String> = listOf()
    private var arguments: MutableList<String> = mutableListOf()

    companion object {
        fun createCommandLine(command: String): CommandLine {
            return CommandLine(command)
        }
    }

    fun `when`(condition: Boolean, thenDo: Consumer<CommandLine>): CommandLine {
        return this.tap { cmd: CommandLine ->
            if (condition) {
                thenDo.accept(cmd)
            }
        }
    }

    fun tap(thenDo: Consumer<CommandLine>): CommandLine {
        thenDo.accept(this)
        return this
    }

    fun withNonArgSecrets(secrets: List<String>): CommandLine {
        this.secrets += secrets
        return this
    }

    fun withArg(argument: String): CommandLine {
        this.arguments += argument
        return this
    }

    fun withArgs(vararg args: String): CommandLine {
        for (arg in args) {
            arguments.add(arg)
        }
        return this
    }

    fun withEncoding(encoding: String): CommandLine {
        this.encoding = encoding
        return this
    }

    // todo: add safe output stream
    fun run() {

    }
}