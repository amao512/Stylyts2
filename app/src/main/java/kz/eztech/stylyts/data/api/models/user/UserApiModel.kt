package kz.eztech.stylyts.data.api.models.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ruslan Erdenoff on 24.12.2020.
 */
data class UserApiModel(
	@SerializedName("id")
	@Expose
	val id: Int?,

	@SerializedName("email")
	@Expose
	val email: String?,

	@SerializedName("initials")
	@Expose
	val initials: String?,

	@SerializedName("username")
	@Expose
	val username: String?,

	@SerializedName("avatar")
	@Expose
	val avatar: String?,

	@SerializedName("first_name")
	@Expose
	val firstName: String?,

	@SerializedName("last_name")
	@Expose
	val lastName: String?,

	@SerializedName("is_brand")
	@Expose
	val isBrand: Boolean,

	@SerializedName("dob")
	@Expose
	val dateOfBirth: String?,

	@SerializedName("gender")
	@Expose
	val gender: String?,

	@SerializedName("web_site")
	@Expose
	val webSite: String?,

	@SerializedName("instagram")
	@Expose
	val instagram: String?,

	@SerializedName("followers_count")
	@Expose
	val followersCount: Int?,

	@SerializedName("followings_count")
	@Expose
	val followingsCount: Int?,

	@SerializedName("outfits_count")
	@Expose
	val outfitsCount: Int?
)