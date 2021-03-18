package kz.eztech.stylyts.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ruslan Erdenoff on 27.02.2021.
 */
@Entity(tableName = "address_table", indices = arrayOf(Index(value = ["id"], unique = true)))
data class AddressEntity(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "last_name")
    var lastName: String? = null,

    @ColumnInfo(name = "phone")
    var phone: String? = null,

    @SerializedName("country")
    @Expose
    @ColumnInfo(name = "country")
    var country: String? = null,

    @SerializedName("city")
    @Expose
    @ColumnInfo(name = "city")
    val city: String? = null,

    @SerializedName("street")
    @Expose
    @ColumnInfo(name = "street")
    val street: String? = null,

    @SerializedName("apartment")
    @Expose
    @ColumnInfo(name = "apartment")
    val apartment: String? = null,

    @SerializedName("entrance")
    @Expose
    @ColumnInfo(name = "entrance")
    val entrance: String? = null,

    @SerializedName("floor")
    @Expose
    @ColumnInfo(name = "floor")
    val floor: String? = null,

    @SerializedName("door_phone")
    @Expose
    @ColumnInfo(name = "door_phone")
    val doorPhone: String? = null,

    @SerializedName("postal_code")
    @Expose
    @ColumnInfo(name = "postal_code")
    val postalCode: String? = null,

    @SerializedName("comment")
    @Expose
    @ColumnInfo(name = "comment")
    val comment: String? = null,

    @ColumnInfo(name = "is_default_address")
    var isDefaultAddress: Boolean = false
)

