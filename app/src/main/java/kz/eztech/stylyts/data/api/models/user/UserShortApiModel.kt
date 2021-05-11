package kz.eztech.stylyts.data.api.models.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserShortApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("username")
    @Expose
    val username: String?,
    @SerializedName("first_name")
    @Expose
    val firstName: String?,
    @SerializedName("last_name")
    @Expose
    val lastName: String?,
    @SerializedName("avatar")
    @Expose
    val avatar: String?,
    @SerializedName("i_already_follow")
    @Expose
    val isAlreadyFollow: Boolean,
    @SerializedName("is_brand")
    @Expose
    val isBrand: Boolean
)