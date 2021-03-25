package kz.eztech.stylyts.domain.models

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
data class CollectionFilterModel(
    var id: Int? = null,
    var name: String? = "Классика",
    var icon: Int? = null,
    var gender: String? = "M",
    var mode: Int = 0,
    var isChosen: Boolean = false
)