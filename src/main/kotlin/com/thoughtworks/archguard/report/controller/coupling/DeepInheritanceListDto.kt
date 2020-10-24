package com.thoughtworks.archguard.report.controller.coupling

import com.thoughtworks.archguard.report.domain.coupling.deepinheritance.DeepInheritance

data class DeepInheritanceListDto(val data: List<DeepInheritance>, val count: Long,
                                  val currentPageNumber: Long, val threshold: Int)