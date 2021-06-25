package kz.eztech.stylyts.domain.models.common

import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

data class SearchFilterModel(
    var query: String = EMPTY_STRING,
    var isBrand: Boolean = false
)