package kz.eztech.stylyts.search.data.db

import android.app.Application
import io.reactivex.Completable
import io.reactivex.Observable
import kz.eztech.stylyts.common.data.db.LocalDatabase

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

    fun deleteUserSearch(userId: Int): Completable {
        return Completable.fromAction {
            userSearchDao.delete(id = userId)
        }
    }

    fun deleteAllUserSearch(): Completable {
        return Completable.fromAction {
            userSearchDao.deleteAll()
        }
    }
}