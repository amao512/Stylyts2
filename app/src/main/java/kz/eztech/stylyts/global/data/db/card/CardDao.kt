package kz.eztech.stylyts.global.data.db.card

import androidx.room.*
import io.reactivex.Observable

/**
 * Created by Ruslan Erdenoff on 02.03.2021.
 */
@Dao
interface CardDao {

	@get:Query("SELECT * FROM card_table")
	val all: Observable<List<CardEntity>>
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(item: CardEntity)
	
	@Query("DELETE FROM card_table WHERE id = :id")
	fun delete(id: Int?)
	
	@Query("DELETE FROM card_table")
	fun deleteAll()
	
	@Update
	fun update(purchase: CardEntity)
}