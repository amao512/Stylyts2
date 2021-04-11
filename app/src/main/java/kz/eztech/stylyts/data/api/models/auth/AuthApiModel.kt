package kz.eztech.stylyts.data.api.models.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kz.eztech.stylyts.data.api.models.user.UserApiModel

data class AuthApiModel(
    @SerializedName("token")
    @Expose
    val token: TokenApiModel?,
    @SerializedName("user")
    @Expose
    val user: UserApiModel?
)