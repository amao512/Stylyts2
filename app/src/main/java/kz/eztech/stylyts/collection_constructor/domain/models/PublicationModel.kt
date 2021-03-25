package kz.eztech.stylyts.collection_constructor.domain.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PublicationModel(
    @SerializedName("description")
    @Expose
    val description: String? = null,
    @SerializedName("hidden")
    @Expose
    val hidden: Boolean = false,
    @SerializedName("image_one")
    @Expose
    val imageOne: String? = null,
    @SerializedName("image_two")
    @Expose
    val imageTwo: String? = null,
    @SerializedName("image_three")
    @Expose
    val imageThree: String? = null,
    @SerializedName("image_four")
    @Expose
    val imageFour: String? = null,
    @SerializedName("image_five")
    @Expose
    val imageFive: String? = null,
    @SerializedName("created")
    @Expose
    val created: String? = null,
    @SerializedName("modified")
    @Expose
    val modified: String? = null
) : Parcelable