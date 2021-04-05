package kz.eztech.stylyts.domain.models.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ExistsUsernameModel(
    @SerializedName("exists")
    @Expose
    val exists: Boolean
)