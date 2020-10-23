package com.thoughtworks.archguard.report.domain.redundancy

import com.thoughtworks.archguard.report.domain.models.ClassVO

data class OverGeneralizationPair(val parentClass: ClassVO, val childClass: ClassVO)