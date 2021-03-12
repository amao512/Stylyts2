package kz.eztech.stylyts.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ruslan Erdenoff on 24.12.2020.
 */
data class UserModel(
	@SerializedName("pk")
	@Expose
	val pk: Int?,
	@SerializedName("username")
	@Expose
	val username: String?,
	@SerializedName("email")
	@Expose
	val email: String?,
	@SerializedName("profile")
	@Expose
	val profile: ProfileModel?
)