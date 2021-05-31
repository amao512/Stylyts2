package kz.eztech.stylyts.data.db

import android.app.Application
import io.reactivex.Completable
import io.reactivex.Observable
import kz.eztech.stylyts.data.db.address.AddressDao
import kz.eztech.stylyts.data.db.address.AddressEntity

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
open class LocalDataSource(application: Application) {

    private val addressDao: AddressDao

    open val allAddresses: Observable<List<AddressEntity>>

    init {
        val db = LocalDatabase.getInstance(application)

        addressDao = db.addressDao()
        allAddresses = addressDao.all
    }


    fun insertAddress(item: AddressEntity): Completable {
        return Completable.fromAction {
            addressDao.insert(item)
        }
    }

    fun deleteAddress(item: AddressEntity): Completable {
        return Completable.fromAction {
            addressDao.delete(item.id)
        }
    }

    fun updateAddress(item: AddressEntity): Completable {
        return Completable.fromAction {
            addressDao.update(item)
        }
    }
}