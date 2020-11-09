package com.thoughtworks.archguard.report.controller.coupling



data class SizingMethodRequestDto(val module: String, val className: String, val packageName: String, val name: String)
