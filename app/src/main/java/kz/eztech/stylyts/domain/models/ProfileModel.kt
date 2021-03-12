package kz.eztech.stylyts.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Asylzhan Seytbek on 11.03.2021.
 */
data class ProfileModel(
    @SerializedName("owner")
    @Expose
    val owner: Int?,
    @SerializedName("avatar")
    @Expose
    val avatar: String?,
    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("last_name")
    @Expose
    val lastName: String?,
    @SerializedName("brand")
    @Expose
    val brand: Boolean,
    @SerializedName("date_of_birth")
    @Expose
    val date_of_birth: String?,
    @SerializedName("gender")
    @Expose
    val gender: String?,
    @SerializedName("is_active")
    @Expose
    val isActive: Boolean,
    @SerializedName("verification_uuid")
    @Expose
    val verification_uuid: String?
)