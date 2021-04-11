package kz.eztech.stylyts.data.api.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OwnerApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("username")
    @Expose
    val username: String?
)