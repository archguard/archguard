package com.thoughtworks.archgard.scanner2.infrastructure.influx

data class DataClassDtoForWriteInfluxDB(val systemId: Long, val dataClassCount: Int, val dataClassWithOneFieldCount: Int) {
    fun toRequestBody(): String {
        return "data_class_count,system_id=${systemId} " +
                "data_class_count=${dataClassCount}," +
                "data_class_with_one_field_count=${dataClassWithOneFieldCount}"
    }
}
