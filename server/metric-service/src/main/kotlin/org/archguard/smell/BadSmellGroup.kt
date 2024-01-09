package org.archguard.smell

/**
 * The `BadSmellGroup` enum class represents different groups of bad smells in code.
 * Each group is associated with a value and a list of bad smell types.
 *
 * The available bad smell groups are:
 * - COUPLING: Represents the group of bad smells related to high coupling.
 * - SIZING: Represents the group of bad smells related to large code size.
 * - COHESION: Represents the group of bad smells related to low cohesion.
 * - REDUNDANCY: Represents the group of bad smells related to redundant design.
 * - TESTBADSMELL: Represents the group of bad smells related to weak tests.
 * - UNDEFINED: Represents a group that is not found or not defined.
 *
 * Each bad smell group has a value and a list of bad smell types associated with it.
 * The value represents the name or description of the bad smell group.
 * The list of bad smell types represents the specific types of bad smells that belong to the group.
 *
 * The `BadSmellGroup` enum class also provides a companion object with a `getGroup` function.
 * This function takes a `BadSmellType` parameter and returns the corresponding `BadSmellGroup`.
 * If the given bad smell type is not found in any group, the function returns the `UNDEFINED` group.
 *
 * Example usage:
 * ```
 * val group = BadSmellGroup.getGroup(BadSmellType.DATACLUMPS)
 * println(group) // Output: COUPLING
 * ```
 *
 * @property value The value or description of the bad smell group.
 * @property badSmells The list of bad smell types associated with the group.
 * @constructor Creates a new instance of the `BadSmellGroup` enum class with the specified value and bad smells.
 * @see BadSmellType
 */
enum class BadSmellGroup(val value: String, val badSmells: List<BadSmellType>) {
    COUPLING(
        "耦合过高",
        listOf(
            BadSmellType.DATACLUMPS,
            BadSmellType.DEEPINHERITANCE,
            BadSmellType.CLASSHUB,
            BadSmellType.METHODHUB,
            BadSmellType.PACKAGEHUB,
            BadSmellType.MODULEHUB,
            BadSmellType.CYCLEDEPENDENCY
        )
    ),
    SIZING(
        "体量过大",
        listOf(
            BadSmellType.SIZINGMODULES,
            BadSmellType.SIZINGPACKAGE,
            BadSmellType.SIZINGMETHOD,
            BadSmellType.SIZINGCLASS
        )
    ),
    COHESION(
        "内聚不足",
        listOf(
            BadSmellType.DATA_CLASS,
            BadSmellType.SHOTGUN_SURGERY
        )
    ),
    REDUNDANCY(
        "设计冗余",
        listOf(
            BadSmellType.REDUNDANT_ELEMENT,
            BadSmellType.OVER_GENERALIZATION
        )
    ),
    TESTBADSMELL(
        "测试薄弱",
        listOf(
            BadSmellType.IGNORE_TEST,
            BadSmellType.SLEEP_TEST,
            BadSmellType.UN_ASSERT_TEST
        )
    ),
    UNDEFINED("未找到", listOf());

    companion object {
        fun getGroup(badSmellType: BadSmellType): BadSmellGroup {
            return values().find { it.badSmells.contains(badSmellType) } ?: UNDEFINED
        }
    }
}
