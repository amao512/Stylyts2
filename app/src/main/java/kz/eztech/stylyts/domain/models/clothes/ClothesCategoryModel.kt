package kz.eztech.stylyts.domain.models.clothes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClothesCategoryModel(
    val id: Int,
    val clothesType: ClothesTypeModel,
    val title: String,
    val bodyPart: Int
): Parcelable