package kz.eztech.stylyts.global.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ActionApiModel(
    @SerializedName("action")
    @Expose
    val action: String?
)