package kz.eztech.stylyts.global.data.models.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FollowSuccessApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("modified_at")
    @Expose
    val modifiedAt: String?,
    @SerializedName("follower")
    @Expose
    val follower: Int?,
    @SerializedName("followee")
    @Expose
    val followee: Int?
)