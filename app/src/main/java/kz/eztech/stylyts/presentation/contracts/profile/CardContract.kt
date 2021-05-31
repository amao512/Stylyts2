package kz.eztech.stylyts.presentation.contracts.profile

import kz.eztech.stylyts.data.db.card.CardEntity
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

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