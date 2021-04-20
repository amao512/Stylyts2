package kz.eztech.stylyts.domain.models.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kz.eztech.stylyts.domain.models.outfits.ItemLocationModel

/**
 * Created by Asylzhan Seytbek on 11.04.2021.
 */
@Parcelize
data class UserModel(
    val id: Int,
    val email: String,
    val username: String,
    val avatar: String,
    val firstName: String,
    val lastName: String,
    val isBrand: Boolean,
    val dateOfBirth: String,
    val gender: String,
    val webSite: String,
    val instagram: String,
    val followersCount: Int,
    val followingsCount: Int,
    val outfitsCount: Int,

    var userLocation: ItemLocationModel? = null
) : Parcelable