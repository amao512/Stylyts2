package kz.eztech.stylyts.data.db.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "search_user_history_table", indices = arrayOf(Index(value = ["id"], unique = true)))
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
)