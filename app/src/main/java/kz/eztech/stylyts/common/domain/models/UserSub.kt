package kz.eztech.stylyts.common.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ruslan Erdenoff on 04.03.2021.
 */
data class UserSub(
    @SerializedName("id")
    @Expose
    var id:Int? = null,
    @SerializedName("first_name")
    @Expose
    var first_name:String? = null,
    @SerializedName("last_name")
    @Expose
    var last_name:String? = null,
    @SerializedName("avatar")
    @Expose
    var avatar:String? = null,
    @SerializedName("username")
    @Expose
    var username:String? = null
)