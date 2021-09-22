package kz.eztech.stylyts.global.domain.models.clothes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClothesSizeModel(
    var clothesId: Int = 0,
    val size: String,
    val count: Int,
    val price: Int = 0,
    val salePrice: Int = 0
) : Parcelable