package kz.eztech.stylyts.data.db

import android.app.Application
import io.reactivex.Completable
import io.reactivex.Observable
import kz.eztech.stylyts.data.db.address.AddressDao
import kz.eztech.stylyts.data.db.address.AddressEntity
import kz.eztech.stylyts.data.db.dao.CardDao
import kz.eztech.stylyts.data.db.entities.CardEntity

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
open class LocalDataSource(application: Application) {

    private val addressDao: AddressDao
    private val cardDao: CardDao

    open val allAddresses: Observable<List<AddressEntity>>
    open val allCards: Observable<List<CardEntity>>

    init {
        val db = LocalDatabase.getInstance(application)

        addressDao = db.addressDao()
        allAddresses = addressDao.all

        cardDao = db.cardDao()
        allCards = cardDao.all
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

    fun updateCard(item: CardEntity): Completable {
        return Completable.fromAction {
            cardDao.update(item)
        }
    }


    fun insertCard(item: CardEntity): Completable {
        return Completable.fromAction {
            cardDao.insert(item)
        }
    }
}