package kz.eztech.stylyts.common.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kz.eztech.stylyts.profile.data.db.dao.AddressDao
import kz.eztech.stylyts.profile.data.db.dao.CardDao
import kz.eztech.stylyts.common.data.db.dao.CartDao
import kz.eztech.stylyts.search.data.db.UserSearchDao
import kz.eztech.stylyts.profile.data.db.entities.AddressEntity
import kz.eztech.stylyts.common.data.db.entities.CardEntity
import kz.eztech.stylyts.common.data.db.entities.CartEntity
import kz.eztech.stylyts.search.data.db.UserSearchEntity

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
@Database(entities = [CartEntity::class, AddressEntity::class, CardEntity::class, UserSearchEntity::class], version = 1)
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