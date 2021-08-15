package kz.eztech.stylyts.profile.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WardrobeApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("modified_at")
    @Expose
    val modifiedAt: String?,
    @SerializedName("user")
    @Expose
    val user: Int?,
    @SerializedName("clothes")
    @Expose
    val clothes: Int?
)