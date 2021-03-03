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
@Entity(tableName = "address_table",indices = arrayOf(Index(value = ["id"],unique = true)))
data class AddressEntity(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    var name: String? = null,

    @SerializedName("surname")
    @Expose
    @ColumnInfo(name = "surname")
    var surname: String? = null,

    @SerializedName("contact")
    @Expose
    @ColumnInfo(name = "contact")
    var contact: String? = null,

    @SerializedName("phone")
    @Expose
    @ColumnInfo(name = "phone")
    var phone: String? = null,

    @SerializedName("country")
    @Expose
    @ColumnInfo(name = "country")
    var country: String? = null,

    @SerializedName("address")
    @Expose
    @ColumnInfo(name = "address")
    var address: String? = null,

    @SerializedName("point")
    @Expose
    @ColumnInfo(name = "point")
    var point: String? = null,

    @SerializedName("postIndex")
    @Expose
    @ColumnInfo(name = "postIndex")
    var postIndex: String? = null,
)

