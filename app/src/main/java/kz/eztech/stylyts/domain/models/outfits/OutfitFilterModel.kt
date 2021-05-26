package kz.eztech.stylyts.domain.models.outfits

import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

data class OutfitFilterModel(
    var userId: Int = 0,
    var gender: String = EMPTY_STRING,
    var isMy: Boolean = false,
    var minPrice: Int = 0,
    var maxPrice: Int = 0,
    var page: Int = 1,
    var isLastPage: Boolean = false
)