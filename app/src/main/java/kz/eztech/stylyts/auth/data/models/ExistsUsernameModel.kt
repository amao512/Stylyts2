package kz.eztech.stylyts.auth.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ExistsUsernameModel(
    @SerializedName("exists")
    @Expose
    val exists: Boolean
)