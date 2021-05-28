package kz.eztech.stylyts.data.db.search

import android.app.Application
import io.reactivex.Completable
import io.reactivex.Observable
import kz.eztech.stylyts.data.db.LocalDatabase

/**
 * Created by Asylzhan Seytbek on 22.03.2021.
 */
open class SearchDataSource(application: Application) {

    private val userSearchDao: UserSearchDao

    open val allUserSearchHistory: Observable<List<UserSearchEntity>>

    init {
        val db = LocalDatabase.getInstance(application)

        userSearchDao = db.userSearchDao()
        allUserSearchHistory = userSearchDao.all
    }

    fun insertUserSearch(user: UserSearchEntity): Completable {
        return Completable.fromAction {
            userSearchDao.insert(user)
        }
    }

    fun updateUserSearch(user: UserSearchEntity): Completable {
        return Completable.fromAction {
            userSearchDao.update(user)
        }
    }

    fun deleteUserSearch(user: UserSearchEntity): Completable {
        return Completable.fromAction {
            userSearchDao.delete(user)
        }
    }

    fun deleteAllUserSearch(): Completable {
        return Completable.fromAction {
            userSearchDao.deleteAll()
        }
    }
}