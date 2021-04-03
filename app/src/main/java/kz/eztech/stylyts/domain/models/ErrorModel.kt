package kz.eztech.stylyts.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ErrorModel(
    @SerializedName("detail")
    @Expose
    val detail: String?,

    @SerializedName("code")
    @Expose
    val code: String?
)