package org.archguard.smell

class BadSmellOverviewItem(eBadSmell: BadSmellType, var level: BadSmellLevelType, var count: Long) {
    var badSmell: String = eBadSmell.value
    var category: String = BadSmellGroup.getGroup(eBadSmell).value
}
