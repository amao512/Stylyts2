package kz.eztech.stylyts.data.api.models.clothes

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ClothesSizeApiModel(
    @SerializedName("size")
    @Expose
    val size: String?,
    @SerializedName("count")
    @Expose
    val count: Int?
)