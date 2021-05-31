package kz.eztech.stylyts.data.db.card

import android.app.Application
import io.reactivex.Completable
import io.reactivex.Observable
import kz.eztech.stylyts.data.db.LocalDatabase

open class CardDataSource(application: Application) {

    private val cardDao: CardDao

    open val allCards: Observable<List<CardEntity>>

    init {
        val db = LocalDatabase.getInstance(application)

        cardDao = db.cardDao()
        allCards = cardDao.all
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