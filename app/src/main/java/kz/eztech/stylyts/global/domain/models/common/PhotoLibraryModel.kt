package kz.eztech.stylyts.global.domain.models.common

data class PhotoLibraryModel(
    val id: Int,
    val photo: String,
    var number: Int = 0,
    var isChosen: Boolean = false,
    var isMultipleChoice: Boolean = false
)