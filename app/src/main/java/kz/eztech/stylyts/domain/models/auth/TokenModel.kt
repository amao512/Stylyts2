package kz.eztech.stylyts.domain.models.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenModel(
    @SerializedName("refresh")
    @Expose
    val refresh: String?,
    @SerializedName("access")
    @Expose
    val access: String?
)