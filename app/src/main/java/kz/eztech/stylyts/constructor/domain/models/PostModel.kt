package kz.eztech.stylyts.constructor.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostModel(
    @SerializedName("description")
    @Expose
    val description: String?,
    @SerializedName("hidden")
    @Expose
    val hidden: Boolean,
    @SerializedName("image_one")
    @Expose
    val imageOne: String?,
    @SerializedName("image_two")
    @Expose
    val imageTwo: String?,
    @SerializedName("image_three")
    @Expose
    val imageThree: String?,
    @SerializedName("image_four")
    @Expose
    val imageFour: String?,
    @SerializedName("image_five")
    @Expose
    val imageFive: String?,
    @SerializedName("created")
    @Expose
    val created: String?,
    @SerializedName("modified")
    @Expose
    val modified: String?
)