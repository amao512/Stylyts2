package kz.eztech.stylyts.domain.models.filter

data class FilterCheckModel(
    val id: Int,
    val item: Any,
    var isChecked: Boolean = false
)