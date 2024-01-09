package org.archguard.threshold

class FlattenThreshold(val dimension: String, val key: String, val condition: String, val value: Int) {
    fun matchKey(dimension: String, key: String, condition: String): Boolean {
        return dimension == this.dimension &&
            key == this.key &&
            condition == (this.condition)
    }
}
