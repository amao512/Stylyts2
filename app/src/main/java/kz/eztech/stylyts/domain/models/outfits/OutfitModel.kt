package kz.eztech.stylyts.domain.models.outfits

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.eztech.stylyts.domain.models.user.UserShortModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel

@Parcelize
data class OutfitModel(
    val id: Int,
    val clothes: List<ClothesModel>,
    val author: UserShortModel,
    var title: String,
    var text: String,
    val gender: String,
    val totalPrice: Int,
    val currency: String,
    val coverPhoto: String,
    val livePhoto: String,
    val clothesLocation: List<ItemLocationModel>,
    val constructorCode: String,
    val saved: Boolean,
    val createdAt: String,
    val modified_at: String,
    val style: Int,
): Parcelable