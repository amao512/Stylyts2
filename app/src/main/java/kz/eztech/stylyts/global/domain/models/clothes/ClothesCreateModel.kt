package kz.eztech.stylyts.global.domain.models.clothes

import kz.eztech.stylyts.global.presentation.common.enums.GenderEnum
import kz.eztech.stylyts.utils.EMPTY_STRING
import java.io.File

data class ClothesCreateModel(
    val id: Int = 0,
    var owner: Int = 0,
    var title: String = EMPTY_STRING,
    var description: String = EMPTY_STRING,
    var gender: String = GenderEnum.MALE.gender,
    var cost: Int = 0,
    var salePrice: Int = 0,
    var coverPhoto: File? = null,
    var clothesType: Int = 0,
    var clothesCategory: Int = 0,
    var clothesStyle: Int = 0,
    var clothesBrand: Int = 0
)