package kz.eztech.stylyts.domain.models.common

import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

data class SearchFilterModel(
    var page: Int = 1,
    var totalPages: Int = 1,
    var isLastPage: Boolean = false,
    var query: String = EMPTY_STRING,
    var isBrand: Boolean = false
)