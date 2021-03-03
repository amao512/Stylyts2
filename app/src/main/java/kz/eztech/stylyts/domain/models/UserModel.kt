package kz.eztech.stylyts.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ruslan Erdenoff on 24.12.2020.
 */
data class UserModel(
	@SerializedName("id")
	@Expose
	var id:Int? = null,
	@SerializedName("followings_count")
	@Expose
	var followings_count:Int? = null,
	@SerializedName("followers_count")
	@Expose
	var followers_count:Int? = null,
	@SerializedName("is_brand")
	@Expose
	var is_brand:Boolean? = null,
	@SerializedName("last_login")
	@Expose
	var last_login:String? = null,
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
	@SerializedName("liked_brands")
	@Expose
	var liked_brands:List<Any>? = null,
	@SerializedName("liked_clothes")
	@Expose
	var liked_clothes:List<Any>? = null,
	@SerializedName("password")
	@Expose
	var password:String? = null,
	@SerializedName("token")
	@Expose
	var token:String? = null,
	@SerializedName("username")
	@Expose
	var username:String? = null,
)