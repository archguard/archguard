package com.thoughtworks.archguard.report.domain

import com.thoughtworks.archguard.report.controller.coupling.FilterSizingDto
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


    fun validFilterParam(requestFilter: FilterSizingDto): FilterSizingDto {
        return FilterSizingDto(
                requestFilter.currentPageNumber,
                requestFilter.numberPerPage,
                validParam(requestFilter.module),
                validParam(requestFilter.className),
                validParam(requestFilter.packageName),
                validParam(requestFilter.name))
    }


    private fun validParam(filterKeyword: String?): String {

        return if (filterKeyword == null || filterKeyword == "" || filterKeyword.contains("'") || filterKeyword.contains("\"")) {
            ""
        } else {
            filterKeyword
        }
    }

}