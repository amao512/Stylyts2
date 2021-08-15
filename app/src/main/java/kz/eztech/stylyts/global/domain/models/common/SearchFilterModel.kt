package kz.eztech.stylyts.global.domain.models.common

import kz.eztech.stylyts.utils.EMPTY_STRING

data class SearchFilterModel(
    var query: String = EMPTY_STRING,
    var isBrand: Boolean = false
)