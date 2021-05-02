package kz.eztech.stylyts.domain.models.wardrobe

import kz.eztech.stylyts.presentation.enums.GenderEnum
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import java.io.File

data class ClothesCreateModel(
    var title: String = EMPTY_STRING,
    var description: String = EMPTY_STRING,
    var gender: String = GenderEnum.MALE.gender,
    var coverPhoto: File? = null,
    var clothesType: Int = 0,
    var clothesCategory: Int = 0,
    var clothesStyle: Int = 0,
)