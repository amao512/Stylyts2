package kz.eztech.stylyts.domain.models.outfits

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.eztech.stylyts.domain.models.clothes.ClothesModel

@Parcelize
data class OutfitCreateModel(
    val id: Int = 0,
    var title: String?,
    var clothesIdList: List<Int>,
    var clothes: List<ClothesModel>,
    var itemLocation: List<ItemLocationModel>,
    var style: Int,
    var authorId: Int,
    var totalPrice: Float = 0f,
    var text: String?,
) : Parcelable