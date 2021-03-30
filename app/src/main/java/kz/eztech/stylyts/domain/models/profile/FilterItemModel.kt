package kz.eztech.stylyts.domain.models.profile

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterItemModel(
    val title: String,
    var isSelected: Boolean = false
): Parcelable