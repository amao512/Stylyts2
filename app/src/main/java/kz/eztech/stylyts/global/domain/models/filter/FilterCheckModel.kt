package kz.eztech.stylyts.global.domain.models.filter

data class FilterCheckModel(
    val id: Int,
    val item: Any,
    var isChecked: Boolean = false,
    var isCustom: Boolean = false
)