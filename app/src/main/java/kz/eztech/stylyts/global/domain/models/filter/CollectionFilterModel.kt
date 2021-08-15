package kz.eztech.stylyts.global.domain.models.filter

import kz.eztech.stylyts.global.presentation.common.enums.GenderEnum

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
data class CollectionFilterModel(
    val id: Int,
    var name: String,
    val icon: Int? = null,
    var gender: String? = GenderEnum.MALE.gender,
    var mode: Int = 0,
    var isChosen: Boolean = false,
    var isDisabled: Boolean = false,
    var item: Any? = null
)