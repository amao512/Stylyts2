package kz.eztech.stylyts.domain.models.outfits

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OutfitCreateModel(
    var title: String?,
    var clothes: List<Int>,
    var itemLocation: List<ItemLocationModel>,
    var style: Int,
    var author: Int,
    var totalPrice: Float,
    var text: String?
) : Parcelable