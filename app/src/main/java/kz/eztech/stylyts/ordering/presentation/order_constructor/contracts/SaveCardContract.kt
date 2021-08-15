package kz.eztech.stylyts.ordering.presentation.order_constructor.contracts

import kz.eztech.stylyts.global.data.db.card.CardEntity
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

interface SaveCardContract {

    interface View : BaseView {

        fun processCard(cardEntity: CardEntity)

        fun processSuccessSaving()
    }

    interface Presenter : BasePresenter<View> {

        fun getCardById(cardId: Int)

        fun saveCard(cardEntity: CardEntity)
    }
}