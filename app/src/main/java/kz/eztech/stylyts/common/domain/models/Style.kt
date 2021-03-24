package kz.eztech.stylyts.common.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
data class Style(
    @SerializedName("id")
    @Expose
    var id:Int? = null,
    @SerializedName("title")
    @Expose
    var title:String? = null
)