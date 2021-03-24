package kz.eztech.stylyts.common.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ruslan Erdenoff on 05.02.2021.
 */
data class BrandsModel(
    @SerializedName("results")
    @Expose
    var results:List<BrandModel>? = null,
)
data class BrandModel(
    @SerializedName("id")
    @Expose
    var id:Int? = null,
    @SerializedName("is_brand")
    @Expose
    var is_brand:Boolean? = null,
    @SerializedName("liked")
    @Expose
    var liked:Boolean? = null,
    @SerializedName("last_login")
    @Expose
    var last_login:String? = null,
    @SerializedName("role")
    @Expose
    var role:String? = null,
    @SerializedName("email")
    @Expose
    var email:String? = null,
    @SerializedName("first_name")
    @Expose
    var first_name:String? = null,
    @SerializedName("last_name")
    @Expose
    var last_name:String? = null,
    @SerializedName("date_of_birth")
    @Expose
    var date_of_birth:String? = null,
    @SerializedName("is_verified")
    @Expose
    var is_verified:Boolean? = null,
    @SerializedName("should_send_mail")
    @Expose
    var should_send_mail:Boolean? = null,
    @SerializedName("verification_uuid")
    @Expose
    var verification_uuid:String? = null,
    @SerializedName("created")
    @Expose
    var created:String? = null,
    @SerializedName("modified")
    @Expose
    var modified:String? = null,
    @SerializedName("avatar")
    @Expose
    var avatar:String? = null,
    var isChosen:Boolean = false
)