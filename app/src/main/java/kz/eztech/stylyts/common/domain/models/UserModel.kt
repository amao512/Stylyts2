package kz.eztech.stylyts.common.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ruslan Erdenoff on 24.12.2020.
 */
data class UserModel(
	@SerializedName("id")
	@Expose
	val id: Int?,

	@SerializedName("avatar")
	@Expose
	val avatar: String?,

	@SerializedName("name")
	@Expose
	val name: String?,

	@SerializedName("last_name")
	@Expose
	val lastName: String?,

	@SerializedName("brand")
	@Expose
	val brand: Boolean,

	@SerializedName("date_of_birth")
	@Expose
	val date_of_birth: String?,

	@SerializedName("gender")
	@Expose
	val gender: String?,

	@SerializedName("is_active")
	@Expose
	val isActive: Boolean,

	@SerializedName("username")
	@Expose
	val username: String?,

	@SerializedName("verification_uuid")
	@Expose
	val verification_uuid: String?
)