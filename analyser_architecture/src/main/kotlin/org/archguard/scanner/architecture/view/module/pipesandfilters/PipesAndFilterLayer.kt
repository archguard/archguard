package org.archguard.scanner.architecture.view.module.pipesandfilters

class Pipe(val name: String, val type: PipeType)
enum class PipeType {
    Data,
    Control
}

class Filter(val name: String, val type: FilterType)
enum class FilterType {
    Transform,
    Source,
    Sink
}