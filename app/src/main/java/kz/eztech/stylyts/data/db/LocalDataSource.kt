package kz.eztech.stylyts.data.db

import android.app.Application
import android.location.Address
import android.nfc.cardemulation.CardEmulation
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kz.eztech.stylyts.data.db.dao.AddressDao
import kz.eztech.stylyts.data.db.dao.CardDao
import kz.eztech.stylyts.data.db.dao.CartDao
import kz.eztech.stylyts.data.db.entities.AddressEntity
import kz.eztech.stylyts.data.db.entities.CardEntity
import kz.eztech.stylyts.data.db.entities.CartEntity

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
open class LocalDataSource(application: Application) {

    val cartDao: CartDao
    val addressDao: AddressDao
    val cardDao: CardDao
    open val allItems: Observable<List<CartEntity>>
    open val allAddresses: Observable<List<AddressEntity>>
    open val allCards: Observable<List<CardEntity>>

    init {
        val db = LocalDatabase.getInstance(application)
        cartDao = db.cartDao()
        allItems = cartDao.all
        addressDao = db.addressDao()
        allAddresses = addressDao.all
        cardDao = db.cardDao()
        allCards = cardDao.all
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

    fun insertAddress(item:AddressEntity):Completable{
        return Completable.fromAction {
            addressDao.insert(item)
        }
    }

    fun deleteAddress(item:AddressEntity):Completable{
        return Completable.fromAction {
            addressDao.delete(item.id)
        }
    }
    
    fun updateAddress(item:AddressEntity):Completable{
        return Completable.fromAction {
            addressDao.update(item)
        }
    }

    fun updateCard(item:CardEntity):Completable{
        return Completable.fromAction {
            cardDao.update(item)
        }
    }
    
    
    fun insertCard(item:CardEntity):Completable{
        return Completable.fromAction {
            cardDao.insert(item)
        }
    }

}