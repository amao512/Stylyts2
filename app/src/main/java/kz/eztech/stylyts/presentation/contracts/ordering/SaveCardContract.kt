package kz.eztech.stylyts.presentation.contracts.ordering

import kz.eztech.stylyts.data.db.card.CardEntity
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

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