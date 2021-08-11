package kz.eztech.stylyts.domain.models.clothes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.eztech.stylyts.utils.EMPTY_STRING

@Parcelize
data class ClothesTypeModel(
    val id: Int,
    val title: String,
    var menCoverPhoto: String = EMPTY_STRING,
    var womenCoverPhoto: String = EMPTY_STRING,
    var menConstructorPhoto: String = EMPTY_STRING,
    var womenConstructorPhoto: String = EMPTY_STRING,

    var isExternal: Boolean = false,
    var externalTitle: String? = null,
    var externalImageId: Int = 0,
    var externalType: Int = 0,
    var isChoosen: Boolean = false,
    var chosenClothesTypes: Int? = null,
    var isWiden: Boolean = false
) : Parcelable