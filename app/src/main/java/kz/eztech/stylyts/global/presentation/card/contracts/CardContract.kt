package kz.eztech.stylyts.global.presentation.card.contracts

import kz.eztech.stylyts.global.data.db.card.CardEntity
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 02.03.2021.
 */
interface CardContract {

    interface View: BaseView {

        fun processCards(list: List<CardEntity>)
    }

    interface Presenter: BasePresenter<View> {

        fun getCardList()
    }
}