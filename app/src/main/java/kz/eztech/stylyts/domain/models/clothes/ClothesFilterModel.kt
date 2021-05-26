package kz.eztech.stylyts.domain.models.clothes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.eztech.stylyts.presentation.enums.GenderEnum
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

@Parcelize
data class ClothesFilterModel(
    var gender: String = GenderEnum.MALE.gender,
    var typeIdList: List<ClothesTypeModel> = emptyList(),
    var categoryIdList: List<ClothesCategoryModel> = emptyList(),
    var brandList: List<ClothesBrandModel> = emptyList(),
    var colorList: List<ClothesColorModel> = emptyList(),
    var inMyWardrobe: Boolean = false,
    var owner: String = EMPTY_STRING,
    var page: Int = 1,
    var minPrice: Int = 0,
    var maxPrice: Int = 0,
    var isLastPage: Boolean = false,
    var onlyBrands: Boolean = true
) : Parcelable