package kz.eztech.stylyts.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AuthModel(
    @SerializedName("token")
    @Expose
    val token: String?,
    @SerializedName("user")
    @Expose
    val user: UserModel?
)