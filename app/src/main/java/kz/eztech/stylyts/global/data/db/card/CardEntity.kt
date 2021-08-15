package kz.eztech.stylyts.global.data.db.card

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ruslan Erdenoff on 02.03.2021.
 */
@Entity(tableName = "card_table",indices = arrayOf(Index(value = ["id"],unique = true)))
data class CardEntity(
	@PrimaryKey(autoGenerate = true)
	@SerializedName("id")
	@Expose
	@ColumnInfo(name = "id")
	var id: Int? = null,

	@SerializedName("number")
	@Expose
	@ColumnInfo(name = "number")
	var number: String? = null,

	@SerializedName("holder")
	@Expose
	@ColumnInfo(name = "holder")
	var nameHolder: String? = null,

	@SerializedName("expiring")
	@Expose
	@ColumnInfo(name = "expiring")
	var expDate: String? = null,
)