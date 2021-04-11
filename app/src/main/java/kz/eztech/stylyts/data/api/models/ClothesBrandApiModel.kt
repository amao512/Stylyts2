package kz.eztech.stylyts.data.api.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ClothesBrandApiModel(
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("title")
    @Expose
    val title: String?,
    @SerializedName("website")
    @Expose
    val website: String?,
    @SerializedName("logo")
    @Expose
    val logo: String?,
    @SerializedName("created_at")
    @Expose
    val createdAt: String?,
    @SerializedName("modified_at")
    @Expose
    val modifiedAt: String?
)