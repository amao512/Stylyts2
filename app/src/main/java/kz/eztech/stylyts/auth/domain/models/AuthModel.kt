package kz.eztech.stylyts.auth.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kz.eztech.stylyts.domain.models.UserModel

data class AuthModel(
    @SerializedName("token")
    @Expose
    val token: String?,
    @SerializedName("user")
    @Expose
    val user: UserModel?
)