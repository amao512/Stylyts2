package kz.eztech.stylyts.global.data.db.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "search_user_history_table", indices = [Index(value = ["id"], unique = true)])
data class UserSearchEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "avatar")
    val avatar: String? = null,

    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "last_name")
    val lastName: String? = null,

    @ColumnInfo(name = "brand")
    val brand: Boolean = false,

    @ColumnInfo(name = "username")
    val username: String? = null
) {
    val displayFullName
        get() = "$name $lastName"

    val displayShortName
        get() = "${name?.toUpperCase(Locale.getDefault())?.get(0)}${lastName?.toUpperCase(Locale.getDefault())?.get(0)}"
}