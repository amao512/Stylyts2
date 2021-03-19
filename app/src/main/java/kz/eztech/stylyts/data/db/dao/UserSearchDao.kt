package kz.eztech.stylyts.data.db.dao

import androidx.room.*
import io.reactivex.Observable
import kz.eztech.stylyts.data.db.entities.UserSearchEntity

@Dao
interface UserSearchDao {

    @get:Query("SELECT * FROM search_user_history_table")
    val all: Observable<List<UserSearchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserSearchEntity)

    @Query("DELETE FROM search_user_history_table WHERE id = :id")
    fun delete(id: Int)

    @Query("DELETE FROM search_user_history_table")
    fun deleteAll()

    @Update
    fun update(user: UserSearchEntity)
}