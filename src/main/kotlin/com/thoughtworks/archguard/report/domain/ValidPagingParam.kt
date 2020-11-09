package com.thoughtworks.archguard.report.domain

import com.thoughtworks.archguard.report.controller.coupling.SizingMethodRequestDto
import com.thoughtworks.archguard.report.exception.WrongLimitException
import com.thoughtworks.archguard.report.exception.WrongOffsetException

object ValidPagingParam {
    fun validPagingParam(limit: Long, offset: Long) {
        if (limit <= 0) {
            throw WrongLimitException("limit $limit is smaller than 1")
        }
        if (offset < 0) {
            throw WrongOffsetException("offset $offset is smaller than 0")
        }
    }

    fun validFilterParam(module: String?, className: String?, packageName: String?, name: String?): SizingMethodRequestDto {

        return SizingMethodRequestDto(
                validParam(module),
                validParam(className),
                validParam(packageName),
                validParam(name))

    }


    private fun validParam(filterKeyword: String?): String {

        return if (filterKeyword == null || filterKeyword == "" || filterKeyword == "''" || filterKeyword.contains("\"")) {
            ""
        } else {
            filterKeyword
        }
    }

}