package kz.eztech.stylyts.domain.models.clothes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClothesTypeModel(
    val id: Int,
    val title: String,
    val menCoverPhoto: String,
    val womenCoverPhoto: String,

    var isExternal: Boolean = false,
    var externalTitle: String? = null,
    var externalImageId: Int = 0,
    var externalType: Int = 0,
    var isChoosen: Boolean = false,
    var chosenClothesTypes: Int? = null
) : Parcelable