package kz.eztech.stylyts.data.api.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ActionApiModel(
    @SerializedName("action")
    @Expose
    val action: String?
)