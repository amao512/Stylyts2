package kz.eztech.stylyts.domain.models.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kz.eztech.stylyts.data.api.models.user.UserApiModel

data class AuthModel(
    @SerializedName("token")
    @Expose
    val token: TokenModel?,
    @SerializedName("user")
    @Expose
    val user: UserApiModel?
)