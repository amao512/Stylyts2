package kz.eztech.stylyts.data.api.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResultsApiModel<T>(
    @SerializedName("page")
    @Expose
    val page: Int?,
    @SerializedName("total_pages")
    @Expose
    val totalPages: Int?,
    @SerializedName("page_size")
    @Expose
    val pageSize: Int?,
    @SerializedName("total_count")
    @Expose
    val totalCount: Int?,
    @SerializedName("results")
    @Expose
    val results: List<T>?
)