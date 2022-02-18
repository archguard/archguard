package com.thoughtworks.archguard.evolution.domain

data class BadSmellSuiteWithSelected(val id: Long, val suiteName: String, val isDefault: Boolean, var isSelected: Boolean, val thresholds: List<BadSmellGroup>)

data class BadSmellSuite(val id: Long, val suiteName: String, val isDefault: Boolean, val thresholds: List<BadSmellGroup>)

data class BadSmellGroup(val name: String, val threshold: List<BadSmellThreshold>)

data class BadSmellThreshold(val name: String, val condition: String, val value: Int)