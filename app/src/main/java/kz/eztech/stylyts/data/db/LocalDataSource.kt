package kz.eztech.stylyts.data.db

import android.app.Application
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kz.eztech.stylyts.data.db.dao.CartDao
import kz.eztech.stylyts.data.db.entities.CartEntity

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
open class LocalDataSource(application: Application) {

    val cartDao: CartDao
    open val allItems: Observable<List<CartEntity>>

    init {
        val db = LocalDatabase.getInstance(application)
        cartDao = db.cartDao()
        allItems = cartDao.all
    }


    fun insert(item:CartEntity):Completable {
        return Completable.fromAction {
            cartDao.insert(item)
        }
    }

    fun delete(item: CartEntity):Completable {
        return Completable.fromAction {
            cartDao.delete(item.id)
        }
    }

}