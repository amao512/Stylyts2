package kz.eztech.stylyts.global.data.db.common

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kz.eztech.stylyts.global.data.db.address.AddressDao
import kz.eztech.stylyts.global.data.db.address.AddressEntity
import kz.eztech.stylyts.ordering.data.db.cart.CartDao
import kz.eztech.stylyts.ordering.data.db.cart.CartEntity
import kz.eztech.stylyts.global.data.db.card.CardDao
import kz.eztech.stylyts.global.data.db.card.CardEntity
import kz.eztech.stylyts.global.data.db.search.UserSearchDao
import kz.eztech.stylyts.global.data.db.search.UserSearchEntity

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
@Database(entities = [CartEntity::class, AddressEntity::class, CardEntity::class, UserSearchEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {

    //dao
    abstract fun cartDao(): CartDao

    abstract fun addressDao(): AddressDao

    abstract fun cardDao(): CardDao

    abstract fun userSearchDao(): UserSearchDao

    companion object {

        //thread safety singleton
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            //thread safety checking
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "stylyts_database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance

                return instance
            }
        }
    }
}