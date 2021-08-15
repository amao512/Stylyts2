package kz.eztech.stylyts.auth.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kz.eztech.stylyts.global.data.models.user.UserApiModel

data class AuthApiModel(
    @SerializedName("token")
    @Expose
    val token: TokenApiModel?,
    @SerializedName("user")
    @Expose
    val user: UserApiModel?
)