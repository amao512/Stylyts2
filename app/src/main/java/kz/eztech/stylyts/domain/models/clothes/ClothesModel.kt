package kz.eztech.stylyts.domain.models.clothes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.eztech.stylyts.domain.models.OwnerModel

@Parcelize
data class ClothesModel(
    val id: Int,
    val clothesStyle: ClothesStyleModel,
    val clothesCategory: ClothesCategoryModel,
    val constructorImage: String,
    val coverImages: List<String>,
    val clothesSizes: List<String>,
    val clothesColors: List<String>,
    val title: String,
    val description: String,
    val gender: String,
    val cost: Int,
    val saleCost: Int,
    val currency: String,
    val productCode: String,
    val createdAt: String,
    val modifiedAt: String,
    val owner: OwnerModel,
    val clothesBrand: ClothesBrandModel,
) : Parcelable