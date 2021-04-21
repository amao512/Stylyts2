package kz.eztech.stylyts.domain.models.filter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

@Parcelize
data class FilterModel(
    var gender: String = EMPTY_STRING,
    var typeIdList: List<Int> = emptyList(),
    var categoryIdList: List<Int> = emptyList(),
    var brandIdList: List<Int> = emptyList(),
    var isMyWardrobe: Boolean = false
) : Parcelable