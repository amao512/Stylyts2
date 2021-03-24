package kz.eztech.stylyts.common.domain.models

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
data class CollectionFilterModel(
    var name:String? = "Классика",
    var id:Int? = null,
    var gender:String? = "M",
    var mode:Int = 0,
    var isChosen:Boolean = false
)