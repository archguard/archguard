package org.archguard.aaac.repl

import java.io.OutputStream

class ReplOutput: OutputStream() {
    override fun write(b: Int) {
        // todo
        println(b)
    }

}

// todo: output to println or logs
class ReplContext(val out: ReplOutput = ReplOutput()) {

}
