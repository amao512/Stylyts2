package kz.eztech.stylyts.auth.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenApiModel(
    @SerializedName("refresh")
    @Expose
    val refresh: String?,
    @SerializedName("access")
    @Expose
    val access: String?
)