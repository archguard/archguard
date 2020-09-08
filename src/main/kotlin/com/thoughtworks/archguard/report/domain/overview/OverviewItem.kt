package com.thoughtworks.archguard.report.domain.overview

class OverviewItem(eBadSmell: BadSmell, eCategory: BadSmellCategory, var count: Long) {
    var badSmell: String = eBadSmell.value
    var category: String = eCategory.value
}
