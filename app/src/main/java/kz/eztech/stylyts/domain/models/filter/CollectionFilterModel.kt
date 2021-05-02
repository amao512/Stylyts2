package kz.eztech.stylyts.domain.models.filter

import kz.eztech.stylyts.presentation.enums.GenderEnum

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
data class CollectionFilterModel(
    var id: Int? = null,
    var name: String? = "Классика",
    var icon: Int? = null,
    var gender: String? = GenderEnum.MALE.gender,
    var mode: Int = 0,
    var isChosen: Boolean = false,
    var item: Any? = null
)