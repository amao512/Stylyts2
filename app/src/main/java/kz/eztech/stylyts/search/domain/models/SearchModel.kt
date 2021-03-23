package kz.eztech.stylyts.search.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchModel<T>(
    @SerializedName("count")
    @Expose
    val count: Int?,
    @SerializedName("next")
    @Expose
    val next: Int?,
    @SerializedName("previous")
    @Expose
    val previous: Int?,
    @SerializedName("results")
    @Expose
    val results: List<T>?
)