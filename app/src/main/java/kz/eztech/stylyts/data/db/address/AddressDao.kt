package kz.eztech.stylyts.data.db.address

import androidx.room.*
import io.reactivex.Observable

/**
 * Created by Ruslan Erdenoff on 27.02.2021.
 */
@Dao
interface AddressDao {
    @get:Query("SELECT * FROM address_table")
    val all: Observable<List<AddressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: AddressEntity)

    @Query("DELETE FROM address_table WHERE id = :id")
    fun delete(id: Int?)

    @Query("DELETE FROM address_table")
    fun deleteAll()
    
    @Update
    fun update(purchase: AddressEntity)
}