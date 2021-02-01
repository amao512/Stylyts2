package kz.eztech.stylyts.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kz.eztech.stylyts.data.db.dao.CartDao
import kz.eztech.stylyts.data.db.entities.CartEntity

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
@Database(entities = [CartEntity::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {

    //dao
    abstract fun cartDao(): CartDao

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
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}