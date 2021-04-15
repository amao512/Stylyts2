package kz.eztech.stylyts.data.db.cart

import android.app.Application
import io.reactivex.Completable
import io.reactivex.Observable
import kz.eztech.stylyts.data.db.LocalDatabase

/**
 * Created by Asylzhan Seytbek on 15.04.2021.
 */
open class CartDataSource(application: Application) {

    private val cartDao: CartDao

    open val allCart: Observable<List<CartEntity>>

    init {
        val db = LocalDatabase.getInstance(application)

        cartDao = db.cartDao()
        allCart = cartDao.all
    }

    fun insert(cart: CartEntity): Completable {
        return Completable.fromAction {
            cartDao.insert(cart)
        }
    }

    fun delete(item: CartEntity): Completable {
        return Completable.fromAction {
            cartDao.delete(item.id)
        }
    }
}