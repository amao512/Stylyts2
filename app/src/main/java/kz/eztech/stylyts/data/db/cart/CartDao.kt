package kz.eztech.stylyts.data.db.cart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
@Dao
interface CartDao {
    @get:Query("SELECT * FROM cart_table")
    val all: Observable<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: CartEntity)

    @Query("DELETE FROM cart_table WHERE id = :id")
    fun delete(id: Int?)

    @Query("DELETE FROM cart_table")
    fun deleteAll()
}