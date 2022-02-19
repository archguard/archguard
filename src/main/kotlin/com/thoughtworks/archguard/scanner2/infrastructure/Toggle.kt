package com.thoughtworks.archguard.scanner2.infrastructure

enum class Toggle(private var status: Boolean) {
    EXCLUDE_INTERNAL_CLASS_CYCLE_DEPENDENCY(true);

    fun getStatus(): Boolean {
        return this.status
    }

    fun setStatus(status: Boolean) {
        this.status = status
    }

}