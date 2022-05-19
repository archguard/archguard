package org.archguard.aaac

import java.util.concurrent.atomic.*

class Connection {
    companion object {
        var lastId = AtomicInteger(0)
    }
}
