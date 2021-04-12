package kz.eztech.stylyts.data.api.models.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FollowerApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("username")
    @Expose
    val username: String?,
    @SerializedName("i_already_follow")
    @Expose
    val isAlreadyFollow: Boolean
)