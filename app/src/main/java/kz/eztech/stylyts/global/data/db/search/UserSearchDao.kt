package kz.eztech.stylyts.global.data.db.search

import androidx.room.*
import io.reactivex.Observable

@Dao
interface UserSearchDao {

    @get:Query("SELECT * FROM search_user_history_table")
    val all: Observable<List<UserSearchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserSearchEntity)

    @Delete
    fun delete(user: UserSearchEntity)

    @Query("DELETE FROM search_user_history_table")
    fun deleteAll()

    @Update
    fun update(user: UserSearchEntity)
}