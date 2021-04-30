package kz.eztech.stylyts.domain.models.filter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

@Parcelize
data class FilterModel(
    var userId: Int = 0,
    var username: String = EMPTY_STRING,
    var gender: String = EMPTY_STRING,
    var typeIdList: List<Int> = emptyList(),
    var categoryIdList: List<Int> = emptyList(),
    var brandList: List<ClothesBrandModel> = emptyList(),
    var colorList: List<Int> = emptyList(),
    var isMyWardrobe: Boolean = false,
    var page: Int = 1,
    var isLastPage: Boolean = false,
) : Parcelable